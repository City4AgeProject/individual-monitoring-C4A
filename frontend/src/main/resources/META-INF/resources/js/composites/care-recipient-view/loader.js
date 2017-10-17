define(['ojs/ojcore', 'text!./composites/care-recipient-view/view.html'
        , './composites/care-recipient-view/viewModel'
        , 'text!./composites/care-recipient-view/metadata.json'
        , 'ojs/ojcomposite','ojs/ojknockout'],
  function(oj, view, viewModel, metadata) {
    oj.Composite.register('care-recipient-view', {
      view: {inline: view}, 
      viewModel: {inline: viewModel},
      metadata: {inline: JSON.parse(metadata)}
    });
  }
);