'use strict';

describe('Controller: ExportCtrl', function () {

  // load the controller's module
  beforeEach(module('publicApp'));

  var ExportCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ExportCtrl = $controller('ExportCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ExportCtrl.awesomeThings.length).toBe(3);
  });
});
