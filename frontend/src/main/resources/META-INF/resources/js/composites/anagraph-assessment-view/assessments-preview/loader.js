define(['ojs/ojcore', 'text!./composites/anagraph-assessment-view/assessments-preview/view.html'
        , './composites/anagraph-assessment-view/assessments-preview/viewModel'
        , 'text!./composites/anagraph-assessment-view/assessments-preview/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('assessments-preview', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);