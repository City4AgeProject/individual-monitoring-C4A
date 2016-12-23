define(['ojs/ojcore', 'text!./view.html', './viewModel', 'ojs/ojcomposite'],
  function(oj, view, viewModel) {
    oj.Composite.register('data-set-diagram', {
      view: {inline: view}, 
      viewModel: {inline: viewModel}
    });
  }
);