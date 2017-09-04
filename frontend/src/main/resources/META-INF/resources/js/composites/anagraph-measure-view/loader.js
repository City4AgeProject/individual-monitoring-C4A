define(['ojs/ojcore', 'text!./composites/anagraph-measure-view/view.html'
        , './composites/anagraph-measure-view/viewModel'
        , 'text!./composites/anagraph-measure-view/metadata.json'
        , 'ojs/ojcomposite'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('anagraph-measure-view', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);