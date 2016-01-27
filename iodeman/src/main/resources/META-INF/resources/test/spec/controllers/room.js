'use strict';

describe('Controller: RoomCtrl', function () {

  // load the controller's module
  beforeEach(module('publicApp'));

  var RoomCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    RoomCtrl = $controller('RoomCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(RoomCtrl.awesomeThings.length).toBe(3);
  });
});
