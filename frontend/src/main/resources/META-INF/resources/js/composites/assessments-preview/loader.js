define(['ojs/ojcore', 'text!./composites/assessments-preview/view.html'
        , './composites/assessments-preview/viewModel'
        , 'text!./composites/assessments-preview/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('assessments-preview', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);