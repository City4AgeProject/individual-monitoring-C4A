define(['ojs/ojcore'
        , 'text!./composites/add-assessment/view.html'
        , './composites/add-assessment/viewModel'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel) {
    oj.Composite.register('add-assessment', {
      view: {inline: view}, 
      viewModel: {inline: viewModel}
    });
  }
);