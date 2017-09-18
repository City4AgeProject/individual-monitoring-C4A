package eu.city4age.dashboard.api.pojo.json.view;

public class View {

	public interface BaseView {}

	public interface AssessmentView extends BaseView {}

	public interface TimeIntervalView extends BaseView {}

	public interface VariationMeasureValueView extends BaseView {}
	
	public interface NUIView extends VariationMeasureValueView {}

}