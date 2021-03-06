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


/***************************************************************
* Usage:: <input type="text" ng-enter="doThisFunction()" />
***************************************************************/
// app.directive('enterEvent', function () {
//   return function(scope, element, attrs) {
//     element.bind('keydown keypress', function(event) {
//       if(event.which === 13) {
//         scope.$apply(function(){
//           scope.$eval(attrs.ngEnter, {'event': event});
//         });
//         event.preventDefault();
//       }
//     });
//   };
// });


/***************************************************************
* Usage:: <input type="text" on-enter="doThisFunction()" />
***************************************************************/
app.directive('onEnter',function(){
  var linkFn = function(scope,element,attrs) {
    element.bind('keypress', function(event) {
      if(event.which === 13) {
        scope.$apply(function() {
          scope.$eval(attrs.onEnter);
        });
        event.preventDefault();
      }
    });
  };
  return {
    link:linkFn
  };
});