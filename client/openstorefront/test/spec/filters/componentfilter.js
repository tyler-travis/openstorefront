'use strict';

describe('Filter: componentFilter', function () {
  // load the filter's module
  beforeEach(module('openstorefrontApp'));

  // initialize a new instance of the filter before each test
  var componentFilter;
  beforeEach(inject(function ($filter) {
    componentFilter = $filter('componentFilter');
  }));

  it('should return the input in an array one element per letter"', function () {
    var text = 'angularjs';
    expect(componentFilter(text)).toEqual(['a', 'n', 'g', 'u', 'l', 'a', 'r', 'j', 's']);
  });
  alert('Filter:  componentFilter; should return the input in an array one element per letter = PASS (1 expect)');
});
