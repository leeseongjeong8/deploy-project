package com.vtw.deploy.deploy.service;

import java.util.List;
import java.util.Map;

import com.vtw.deploy.common.fileupload.dto.FileDTO;
import com.vtw.deploy.deploy.dto.DeployDTO;
import com.vtw.deploy.script.dto.ScriptDTO;


public interface DeployService {	
	
	public int insertDeploy(DeployDTO deploy) throws Exception;
	
	public List<ScriptDTO> selectScriptDetail(int deployNo);
	
	public Map<String, Object> selectDeployDetail(int deployNo);
	
	public Map<String, Object> selectDeploySearch(String searchCategory, String keyword);
	
	public FileDTO selectDeployFile(int deployNo);
	
	public int updateDeployState(DeployDTO deploy);
	
	public List<DeployDTO> selectTeamDeploy(String codeName);
	
	public Map<String, Object> selectTeamDeployList(String codeName);

}
