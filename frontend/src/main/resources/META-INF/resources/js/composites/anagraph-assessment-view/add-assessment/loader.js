define(['ojs/ojcore', 'text!./composites/anagraph-assessment-view/add-assessment/view.html'
        , './composites/anagraph-assessment-view/add-assessment/viewModel'
        , 'text!./composites/anagraph-assessment-view/add-assessment/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('add-assessment', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);