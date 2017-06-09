define(['ojs/ojcore', 'text!./composites/assessments-list/view.html'
        , './composites/assessments-list/viewModel'
        , 'text!./composites/assessments-list/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('assessments-list', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);