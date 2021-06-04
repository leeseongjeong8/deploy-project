package com.vtw.deploy.deploy.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vtw.deploy.common.fileupload.dto.FileDTO;
import com.vtw.deploy.common.fileupload.message.ResponseMessage;
import com.vtw.deploy.deploy.dto.DeployDTO;
import com.vtw.deploy.deploy.service.DeployService;
import com.vtw.deploy.script.dto.ScriptDTO;

@RestController
@RequestMapping({ "/deploy" })
public class DeployController {

	@Autowired
	private DeployService deployService;
	Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	//1. 배포이력 추가
	@PostMapping
	public ResponseEntity<ResponseMessage> insertDeploy(@RequestBody DeployDTO deploy) throws Exception {
		String message = "";
		int result = deployService.insertDeploy(deploy);
		System.out.println("else"+result);
		if(result>0) {
			message = "Successfully Inserted Deploy";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, true, true));		
		}else{
			message = "Failed Fetched Deploy";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, false, false));
		}		
	}
	
	//2. 배포 스크립트 내용
	@GetMapping("/{deployNo}")
	public ResponseEntity<ResponseMessage> selectScriptDetail(@PathVariable int deployNo) {
		String message = "";
		List<ScriptDTO> script = deployService.selectScriptDetail(deployNo);
		if(script != null) {
			message = "Successfully Fetched Scripts";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, script, true));
		} else {
			message = "Failed Fetched Scripts";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, null, false));
		}	
	}

	//3. 배포이력 내용
	@GetMapping("/deployContent/{deployNo}")
	public ResponseEntity<ResponseMessage> selectDeployDetail(@PathVariable int deployNo){
		String message = "";
		
		Map<String,Object> map = deployService.selectDeployDetail(deployNo);
		if(map != null) {
			message = "Successfully Fetched Deploy";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, map, true));
		} else {
			message = "Failed Fetched Deploy";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, null, false));
		}
	}

	//4. 배포이력 검색
	@GetMapping("/search")
	public ResponseEntity<ResponseMessage> selectDeploySearch(@RequestParam String searchCategory, @RequestParam String keyword){   
		String message = "";
		
		Map<String,Object> map = deployService.selectDeploySearch(searchCategory,keyword);
		if(map != null) {
			message = "Successfully Fetched Deploy Contents";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, map, true));
		} else {
			message = "Failed Fetched Deploy Contents";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, null, false));
		} 
	}
	
	//5. deploy 파일경로 및 이름 검색
	@GetMapping("/search/{deployNo}")
	public ResponseEntity<ResponseMessage> selectDeployFile(@PathVariable int deployNo) throws Exception{
		String message = "";
		FileDTO file =  deployService.selectDeployFile(deployNo);
		if(file != null) {
			message = "Successfully Fetched File Contents";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, file, true));
		} else {
			message = "Failed Fetched File Contents";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, null, false));
		}
	}
	
	//6. 배포이력상태 update
	@PatchMapping
	public ResponseEntity<ResponseMessage> updateDeployState(@RequestBody DeployDTO deploy) {
		String message = "";
		
		if(deployService.updateDeployState(deploy) > 0) {
			message = "Successfully Updated DeployState";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,true,true));
		}else {
			message = "Failed To Update DeployState";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,false,true));
		}
	}
	
	//dashboard
	@GetMapping("/team")
	public ResponseEntity<ResponseMessage> selectTeamDeploy(@RequestParam String codeName) {
		String message = "";
		List<DeployDTO> deployList = deployService.selectTeamDeploy(codeName);
		if(deployList!= null) {
			message = "Successfully selected DeployList By Team";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,deployList,true)); 
		} else {
			message = "Failed To select DeployList By Team";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message,false,true));
		}
	}
	
	@GetMapping("/teamlist")
	public ResponseEntity<ResponseMessage> selectTeamDeployList(@RequestParam String codeName){
		String message = "Successfully Fetched TeamDeploy";
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,deployService.selectTeamDeployList(codeName),true));
	}
}
