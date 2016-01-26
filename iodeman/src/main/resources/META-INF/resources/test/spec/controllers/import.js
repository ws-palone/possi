'use strict';

describe('Controller: ImportCtrl', function () {

  // load the controller's module
  beforeEach(module('publicApp'));

  var ImportCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ImportCtrl = $controller('ImportCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ImportCtrl.awesomeThings.length).toBe(3);
  });
});
