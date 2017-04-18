define(['ojs/ojcore', 'text!./composites/data-set-diagram/view.html'
        , './composites/data-set-diagram/viewModel'
        , 'text!./composites/data-set-diagram/metadata.json'
        , 'ojs/ojcomposite', 'ojs/ojknockout', 'ojs/ojchart'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('data-set-diagram', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);