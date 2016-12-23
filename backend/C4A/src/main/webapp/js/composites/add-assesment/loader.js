define(['ojs/ojcore'
        , 'text!./composites/add-assesment/view.html'
        , './composites/add-assesment/viewModel'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel) {
    oj.Composite.register('add-assesment', {
      view: {inline: view}, 
      viewModel: {inline: viewModel}
    });
  }
);