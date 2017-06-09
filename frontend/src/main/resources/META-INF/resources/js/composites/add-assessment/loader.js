define(['ojs/ojcore', 'text!./composites/add-assessment/view.html'
        , './composites/add-assessment/viewModel'
        , 'text!./composites/add-assessment/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('add-assessment', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);