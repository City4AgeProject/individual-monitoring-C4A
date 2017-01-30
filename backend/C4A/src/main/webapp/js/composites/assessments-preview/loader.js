define(['ojs/ojcore', 'text!./composites/assessments-preview/view.html'
        , './composites/assessments-preview/viewModel'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel) {
    oj.Composite.register('assessments-preview', {
      view: {inline: view}, 
      viewModel: {inline: viewModel}
    });
  }
);