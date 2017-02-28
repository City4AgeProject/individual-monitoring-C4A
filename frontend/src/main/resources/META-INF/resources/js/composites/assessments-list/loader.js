define(['ojs/ojcore', 'text!./composites/assessments-list/view.html'
        , './composites/assessments-list/viewModel'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel) {
    oj.Composite.register('assessments-list', {
      view: {inline: view}, 
      viewModel: {inline: viewModel}
    });
  }
);