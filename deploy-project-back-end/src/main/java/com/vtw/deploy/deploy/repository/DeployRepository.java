package com.vtw.deploy.deploy.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.vtw.deploy.deploy.dto.DeployDTO;
/*
 * @author 진영
 */
@Mapper
public interface DeployRepository {
	
	public int insertDeploy(DeployDTO deploy);
	
	@Select("SELECT DEPLOY_DATE FROM DEPLOY.DEPLOY_MANAGEMENT WHERE DEPLOY_NO = #{deployNo}")
	public Date selectDeployDate(int deployNo);
	
	public List<DeployDTO> selectDeploySearch(String searchCategory, String keyword);
	
	public DeployDTO selectDeployDetail(int deployNo);
	
	public String selectDeployZip(int deployNo);
	
	public int updateDeployState(DeployDTO deploy);
	
	public List<DeployDTO> selectTeamDeploy(String codeName);
	
	public List<DeployDTO> selectTeamDeployList(String team);
	
}
