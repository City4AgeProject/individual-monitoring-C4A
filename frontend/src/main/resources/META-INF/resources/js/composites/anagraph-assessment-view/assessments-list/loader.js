define(['ojs/ojcore', 'text!./composites/anagraph-assessment-view/assessments-list/view.html'
        , './composites/anagraph-assessment-view/assessments-list/viewModel'
        , 'text!./composites/anagraph-assessment-view/assessments-list/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('assessments-list', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);