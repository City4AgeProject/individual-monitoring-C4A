define(['ojs/ojcore', 'text!./composites/data-set-diagram/view.html'
        , './composites/data-set-diagram/viewModel'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel) {
    oj.Composite.register('data-set-diagram', {
      view: {inline: view}, 
      viewModel: {inline: viewModel}
    });
  }
);