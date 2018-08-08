package eu.city4age.dashboard.api.pojo.json.clusteredMeasures;

import java.math.BigDecimal;
import java.util.List;

public class ClusteredMeasuresDeserializer {
	
	private List<ClusterDataDeserializer> cluster;
	
	private List<BigDecimal> mean;
	
	private List<BigDecimal> var;
	
	private List<List<BigDecimal>> trans_mat;
	
	private List<String> groups;

	/**
	 * 
	 */
	public ClusteredMeasuresDeserializer() {
	}	

	/**
	 * @param cluster
	 * @param mean
	 * @param var
	 * @param trans_mat
	 * @param groups
	 */
	public ClusteredMeasuresDeserializer(List<ClusterDataDeserializer> cluster, List<BigDecimal> mean,
			List<BigDecimal> var, List<List<BigDecimal>> trans_mat, List<String> groups) {
		this.cluster = cluster;
		this.mean = mean;
		this.var = var;
		this.trans_mat = trans_mat;
		this.groups = groups;		
	}

	/**
	 * @return the cluster
	 */
	public List<ClusterDataDeserializer> getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(List<ClusterDataDeserializer> cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the mean
	 */
	public List<BigDecimal> getMean() {
		return mean;
	}

	/**
	 * @param mean the mean to set
	 */
	public void setMean(List<BigDecimal> mean) {
		this.mean = mean;
	}

	/**
	 * @return the var
	 */
	public List<BigDecimal> getVar() {
		return var;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(List<BigDecimal> var) {
		this.var = var;
	}

	/**
	 * @return the trans_mat
	 */
	public List<List<BigDecimal>> getTrans_mat() {
		return trans_mat;
	}

	/**
	 * @param trans_mat the trans_mat to set
	 */
	public void setTrans_mat(List<List<BigDecimal>> trans_mat) {
		this.trans_mat = trans_mat;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
}
