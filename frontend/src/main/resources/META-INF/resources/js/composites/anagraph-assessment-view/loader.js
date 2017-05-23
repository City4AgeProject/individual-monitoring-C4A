define(['ojs/ojcore', 'text!./composites/anagraph-assessment-view/view.html'
        , './composites/anagraph-assessment-view/viewModel'
        , 'text!./composites/anagraph-assessment-view/metadata.json'
        , 'ojs/ojcomposite', 'ojs/ojknockout', 'ojs/ojchart'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('anagraph-assessment-view', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);