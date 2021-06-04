package com.vtw.deploy.script.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeroturnaround.zip.commons.FileUtils;

import com.jcraft.jsch.JSchException;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.sftp.TransferCancelledException;
import com.sshtools.ssh.ChannelOpenException;
import com.sshtools.ssh.SshException;
import com.vtw.deploy.defaultpath.repository.DefaultPathRepository;
import com.vtw.deploy.deploy.repository.DeployRepository;
import com.vtw.deploy.script.config.DevelopServerConfig;
import com.vtw.deploy.script.config.EipsConfig;
import com.vtw.deploy.script.config.GmdadminConfig;
import com.vtw.deploy.script.dto.ScriptDTO;
import com.vtw.deploy.script.exception.ScriptException;
import com.vtw.deploy.script.repository.ScriptRepository;
import com.vtw.deploy.script.service.ScriptService;
import com.vtw.deploy.script.util.FileUtil;
import com.vtw.deploy.script.util.ScriptUtil;

/*
 * script 생성관련 service
 * @author 이성정
 */
@Service
public class ScriptServiceImpl implements ScriptService {

	@Autowired
	private ScriptUtil scriptUtil;

	@Autowired
	private DefaultPathRepository defaultRepository;

	@Autowired
	private ScriptRepository scriptRepository;

	@Autowired
	private DeployRepository deployRepository;

	@Autowired
	private FileUtil fileUtil;

	@Autowired
	private EipsConfig eipsConfig;

	@Autowired
	private GmdadminConfig gmdadminConfig;

	@Autowired
	private DevelopServerConfig developServerConfig;

	public final String TARGET_PATH = "target/classes";
	public final String JENKINS_COMMAND = "docker cp jenkins:";
	public final String MKDIR_COMMAND = "mkdir -p ";

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ScriptDTO addDirectoryToScript(ScriptDTO script, int no) throws ScriptException {

		Map<String, String> prefixAndSuffixMap = script.getSuffixForDirectory();

		String suffix = prefixAndSuffixMap.get("suffix");
		String fileSuffix = prefixAndSuffixMap.get("fileSuffix");
		String xmlSuffix = prefixAndSuffixMap.get("xmlSuffix");

		String directoryPath = "";

		if (!script.isFileNameNull()) {

			if (no != -1) {

				directoryPath += MKDIR_COMMAND + defaultRepository.selectDevelopTmp() + suffix + "/" + no;
				script.setDirectoryPath(directoryPath);
				script.setPrdTmpDirectory(defaultRepository.selectPrdTmp() + fileSuffix);

			} else if (no == -1) {

				directoryPath += developServerConfig.getDeployedSources() + xmlSuffix + "/xml";
				System.out.println(developServerConfig.getDeployedSources()+  " 이게 deployedSources값");
				script.setDirectoryPath(directoryPath);
				script.setPrdTmpDirectory(defaultRepository.selectDevelopTmp() + xmlSuffix);

			}

		} else {

			throw new ScriptException("fileName이 없습니다.(폴더는 아직 구현되지 않았습니다.)");
		}

		return script;
	}

	@Override
	public ScriptDTO addAllOperationScripts(ScriptDTO script) throws ScriptException {

		String operationPath = defaultRepository.selectOperationPath(script.getDefaultPathByCategoryAndFileType());// default테이블에있는
																													// 운영경로받아온다.
		script.setOperationDirectoryByOperationPath(operationPath); // operationDirectory 를 set
		script.setAllScripts();

		return script;
	}

	@Override
	@Transactional
	public int insertScriptList(List<ScriptDTO> scriptList, String timeStamp, int deployNo) throws ScriptException{

		int insertScriptResult = 0;

		int portalNo = 1;
		int tbwNo = 1;
		int centerNo = 1;

		for (ScriptDTO script : scriptList) {

			script = scriptUtil.addInfoToScript(script); // script에는 localPath UsedPath category fileName fileType 가
															// 들어있음

			script.setTimeStamp(timeStamp);
			script.setDeployNo(deployNo);

			int no = script.getCategoryIndex(portalNo, tbwNo, centerNo);

			script = addDirectoryToScript(script, no); // directoryPath 와 prdTmpDirectory 를 set
			script = addAllOperationScripts(script); // 운영/백업/롤백 스크립트를 set

			insertScriptResult = scriptRepository.insertScript(script);

		}
		return insertScriptResult;
	}

	@Override
	public String createCommand(ScriptDTO script) {

		String jenkinsDirectory = "";
		String path = "";

		scriptUtil.addInfoToScript(script);

		if (script.isJava()) {

			path = script.getPathForCommand();

		} else if (script.isXml()) {

			return "cp " + gmdadminConfig.getTbwappWarPath() + script.splitLocalPathBySrc() + " "
					+ gmdadminConfig.getXmlSources();
		} else {
			path = script.getLocalPath();
		}

		if (script.isTbwapp()) {
			jenkinsDirectory = defaultRepository.selectDevelopTbwapp();
		} else if (script.isPortal()) {
			jenkinsDirectory = defaultRepository.selectDevelopPortal();
		} else if (script.isCenter()) {
			jenkinsDirectory = defaultRepository.selectDevelopCenter();
		}

		return JENKINS_COMMAND + jenkinsDirectory + "/" + path + " " + script.splitDirectoryPathByMkdir();
	}

	public void createCommandMapList(List<ScriptDTO> scriptList, List<Map<String, String>> jenkinsCommandList,
			List<Map<String, String>> xmlCommandList) throws ScriptException {

		for (ScriptDTO script : scriptList) {

			Map<String, String> map = new HashMap<String, String>();

			if (!script.isXml()) {

				script.addMkdirCommandToMap(map);
				map.put("jenkinsCommand", createCommand(script));
				jenkinsCommandList.add(map);

			} else if (script.isXml()) {

				map.put("xmlSrc", createCommand(script)); // cp /home ... /home/ (125->125 커맨드)
				script.addXmlDstAndXmlServerDirToMap(map, developServerConfig.getXmlSources());
				xmlCommandList.add(map);

			} else {

				throw new ScriptException("createCommand가 제대로 수행되지 않았습니다. (in copyFileService)");

			}
		}

	}

	@Override
	public List<ScriptDTO> selectScriptList(int deployNo) {

		return scriptRepository.selectScriptList(deployNo);
	}

	@Override
	public void copyXmlFile(List<Map<String, String>> xmlCommandList) throws ScriptException {

		for (Map<String, String> command : xmlCommandList) {

			File src = new File(command.get("XmlServerDir")); // xmlCommandList를 실행시켜야함 command에는 xml file의 경로
			File dst = new File(command.get("xmlDst")); // db의 directorypath가 xml파일 옮길 경로
			System.out.println(command.get("xmlDst") + " 이값 확인");
			try {
				FileUtils.copyFile(src, dst);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ScriptException("xml파일을 복사해오는데에 실패하였습니다.");
			}
		}
	}

	@Override
	@Transactional
	public Map<String, String> copyFileService(int deployNo) throws ScriptException, JSchException, IOException, RuntimeException, SshException, SftpStatusException, ChannelOpenException, TransferCancelledException {

		String timeStamp = scriptUtil.createTimeStampStr(deployRepository.selectDeployDate(deployNo));

		String directory = defaultRepository.selectDevelopTmp() + "/" + timeStamp; // 126 서버에 있는 jenkins에서 받아온 파일의 경로
		String xmlSrcDirectory = gmdadminConfig.getXmlSources(); // 125 서버에 있는 xml source들의 경로
		String xmlDstDirectory = developServerConfig.getXmlSources(); // 개발 서버에 xml source들 저장할 경로

		String noneZipDirectory = developServerConfig.getDeployedSources() + "/" + timeStamp + "_" + deployNo; // 생성파일이 저장될 개발서버 경로
		String zipDirectory = noneZipDirectory + ".zip"; // zip파일이 저장될 경로

		List<ScriptDTO> scriptList = scriptRepository.selectScriptList(deployNo); // deploy no에 해당하는 스크립트를 불러온다

		List<Map<String, String>> jenkinsCommandList = new ArrayList<Map<String, String>>(); // jenkinsCommand 를 담을
																								// list<map>을만든다.
		List<Map<String, String>> xmlCommandList = new ArrayList<Map<String, String>>(); // xmlComman를 담을 list를 만든다.

		createCommandMapList(scriptList, jenkinsCommandList, xmlCommandList);

		int copyFileResult = 0;

		if (jenkinsCommandList.size() != 0) {

			copyFileResult = fileUtil.getFileFromSource(jenkinsCommandList, "jenkins");

			if (copyFileResult == 1) {

				fileUtil.moveToOtherServer(directory, noneZipDirectory, eipsConfig.getHost(), eipsConfig.getUsername(),
						eipsConfig.getPassword());

			} else {

				throw new ScriptException("jenkins로 부터 파일을 가져오는 것에 실패하였습니다.");

			}
		}

		int getXmlResult = 0;

		if (xmlCommandList.size() != 0) {

			getXmlResult = fileUtil.getFileFromSource(xmlCommandList, "gmdadmin");

			if (getXmlResult == 1) {

				fileUtil.moveToOtherServer(xmlSrcDirectory, xmlDstDirectory, gmdadminConfig.getHost(),
						gmdadminConfig.getUsername(), gmdadminConfig.getPassword());
				copyXmlFile(xmlCommandList);

			} else {

				throw new ScriptException("125로 부터 xml파일을 가져오는 것에 실패하였습니다.");

			}
		}

		// 4.zip으로 묶어준다.
		int zipResult = fileUtil.makeZipFromDir(new File(noneZipDirectory), new File(zipDirectory));

		if (zipResult != 1) {

			throw new ScriptException("zipFile을 다운로드받는데에 실패하였습니다.");

		}

		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("directory", defaultRepository.selectDevelopTmp());
		returnMap.put("fileName", timeStamp + "_" + deployNo + ".zip");

		return returnMap;
	}
	
}
