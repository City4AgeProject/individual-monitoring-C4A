package eu.city4age.dashboard.api.pojo.json.view;

public class View {

	public interface BaseView {}

	public interface AssessmentView extends BaseView {}
	
	public interface GefAssessmentView extends AssessmentView {}
	
	public interface ClusterAssessmentView extends AssessmentView {}

	public interface TimeIntervalView extends BaseView {}

	public interface VariationMeasureValueView extends BaseView {}
	
	public interface NUIView extends VariationMeasureValueView {}
	
	public interface AnalyticsGraphView extends BaseView {}
	
	public interface AnalyticsCSVView extends BaseView {}
	
	public interface AnalyticsCSVCategoryView extends AnalyticsCSVView {}
	
	public interface AnalyticsCSVTimeView extends AnalyticsCSVView {}
	
	public interface AnalyticsCSVTimeCategoryView extends AnalyticsCSVView {}

}