package eu.city4age.dashboard.api.pojo.json.clusteredMeasures;

import java.math.BigDecimal;
import java.util.List;

public class ClusteredMeasuresDeserializer {
	
	private List<ClusterDataDeserializer> cluster;
	
	private List<BigDecimal> mean;
	
	private List<BigDecimal> std;
	
	private List<List<BigDecimal>> trans_mat;
	
	private List<String> groups;
	
	private List<Long> vmvid;

	/**
	 * 
	 */
	public ClusteredMeasuresDeserializer() {
	}	

	/**
	 * @param cluster
	 * @param mean
	 * @param std
	 * @param trans_mat
	 * @param groups
	 */
	public ClusteredMeasuresDeserializer(List<ClusterDataDeserializer> cluster, List<BigDecimal> mean,
			List<BigDecimal> std, List<List<BigDecimal>> trans_mat, List<String> groups, List<Long> vmvid) {
		this.cluster = cluster;
		this.mean = mean;
		this.std = std;
		this.trans_mat = trans_mat;
		this.groups = groups;
		this.vmvid = vmvid;
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

	
	
	public List<BigDecimal> getStd() {
		return std;
	}

	public void setStd(List<BigDecimal> std) {
		this.std = std;
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

	/**
	 * @return the vmvid
	 */
	public List<Long> getVmvid() {
		return vmvid;
	}

	/**
	 * @param vmvid the vmvid to set
	 */
	public void setVmvid(List<Long> vmvid) {
		this.vmvid = vmvid;
	}
	
}
