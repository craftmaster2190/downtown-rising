angular
  .module('rising')
  .component('loremIpsum', {
    templateUrl: 'components/lorem-ipsum/lorem-ipsum.html',
    controller: function(){
    	console.log('Initializing LoremIpsum Controller...');
    }
  });
