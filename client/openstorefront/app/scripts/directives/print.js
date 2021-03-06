/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';

app.directive('print', ['business', '$timeout', '$location', function (Business, $timeout, $location) {
  var getTemplateUrl = function(element, attrs) {
    var type = attrs.type || null;
    // console.log('type', type);
    
    if (type && type === 'component') {
      return 'views/details/print.html';
    } else {
      return 'views/details/print.html';
    }
  };

  return {
    templateUrl: getTemplateUrl,
    restrict: 'EA',
    scope:{
      id: "="
    },
    link: function postLink(scope, element, attrs) {
      scope.details;
      // console.log('scope.id', scope.id);
      
      scope.getObjectContent = function(details) {
        var temp = {};
        temp.value = [];
        var keys = Object.keys(details);
        _.each(keys, function(prop){
          var property = {};
          property.checkedLabel = camelToSentence(prop);
          property.data = details[prop];
          property.checked = true;
          temp[prop] = property;
        })
        if (temp.value){
          delete temp.value;
        }
        temp['badges']={
          checkedLabel: 'Badges',
          checked: true,
          data: ''
        };
        
        // details.checkedLabel = camelToSentence();
        return temp;
      }

      scope.reset = function() {
        if (scope.details) {
          _.each(scope.details, function(detail){
            detail.checked = true;
          });
        }
      }

      /***************************************************************
      * This function converts a timestamp to a displayable date
      ***************************************************************/
      scope.getDate = function(date){
        return utils.getDate(date);
      };

      scope.print = function() {
        window.print();
      }

      scope.formatTags = function(tags) {
        var result = '';
        _.each(tags, function(tag){
          if (result.length > 0) {
            result = result + ", " + tag.text;
          } else {
            result = tag.text;
          }
        });
        return result;
      }

      scope.isEvaluationPresent = function() {
        if (scope.details) {
          var details = scope.details;
          return details.evaluation.checked && details.evaluation.data && details.evaluation.data.evaluationSections && details.evaluation.data.evaluationSections.length;
        } else {
          return false;
        }
      }

      scope.getFullRating = function(num) {
        return new Array(num);   
      }
      scope.getEmptyRating = function(num) {
        var length = ((5 - num) > 0)? (5 - num):0; 
        return new Array(length);
      }

      scope.getTimes = function(n){
        return new Array(parseInt(n));
      };

      if (scope.id){
        Business.componentservice.getComponentPrint(scope.id, true).then(function(result){
          // console.log('Details', result);
          scope.details = result? scope.getObjectContent(result): [];
          // console.log('Details', scope.details);
        }, function() {
          scope.details = [];
        })
      }
    }
  };
}]);
