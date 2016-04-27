$(document).ready(function() {

    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
});

/* PD: Left as an example, not being used in code */
function showDashboard() {
    var pageContentWrapper = $('#page-content-wrapper');
    pageContentWrapper.html("Dashboard contents");
}

(function() {
    var module = angular.module('TheFoodist', []);

    module.config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        $httpProvider.defaults.withCredentials = true;
        delete $httpProvider.defaults.headers.common["X-Requested-With"];
        $httpProvider.defaults.headers.common["Accept"] = "application/json";
        $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
        $httpProvider.defaults.headers.common['Authorization'] = getAuthString();
    }]);

    module.filter('displayFullName', function() {
        return function(userInfoObj) {
            return userInfoObj.firstName + " " + userInfoObj.lastName;
        }    
    });
    
    module.controller('MainController', ['$scope', '$timeout', '$http', function ($scope, $timeout, $http) {
        
        /* AP: Index 0 = tableView, Index 1 = cardView */
        $scope.tabIndex = 0;

        /* AP: Index 0 = sortByPopular, Index 1 = sortByName, Index 2 = sortByCuisine, Index 3 = sortByMeal */
        $scope.sortIndex = 0;
        
        $scope.searchQuery = "";

        $scope.userInfo = {};
        $scope.listOfItems = [];

        $scope.getUserInfo = function() {
            $http({
                url: getAPIURL() + "/user-info",
                method: "GET"
            }).success(function(data, status, headers, config) {
                if(data.userInfo != undefined && data.userInfo != null) {
                    $scope.userInfo = data.userInfo;
                }
            }).error(function (data, status, headers, config) {
                //alert("Something went wrong");
            });
        };

        $scope.getUserInfo();

        $scope.getListOfItems = function() {
            $http({
                url: getAPIURL() + "/items",
                method: "GET"
            }).success(function(data, status, headers, config) {
                if(data != null && data.items != undefined && data.items != null) {
                    $scope.listOfItems = data.items;

                    $timeout(function (){
                        $("#card-wrapper").isotope({
                            itemSelector: 'cardItem',
                            getSortData : {
                                popular: function(e) {
                                    return parseInt($(e).find('.item-saved').find('h4').text());
                                },
                                name: function(e) {
                                    return $(e).find('.front').find('.content').find('.main').find('h3').text()
                                },
                                cuisine: function(e) {
                                    return $(e).find('.cuisine-tag').text()
                                },
                                meal: function(e) {
                                    return $(e).find('.meal-tag').text()
                                }
                            },
                            sortBy: 'popular',
                            sortAscending: true,
                            masonry : {
                                columnWidth: 350,
                                isFitWidth: true
                            }
                        });
                    }, 500);
                }
            }).error(function (data, status, headers, config) {
                //alert("Something went wrong");
            });
        };
        
        $scope.getListOfItems();

        $scope.sortByPopular = function() {
            if($scope.sortIndex != 0) {
                $("#card-wrapper").isotope({sortBy: 'popular'});
                $scope.sortIndex = 0;
            }
        };

        $scope.sortByName  = function() {
            if($scope.sortIndex != 1 && $scope.tabIndex == 1) {
                $("#card-wrapper").isotope({sortBy: 'name'});
                $scope.sortIndex = 1;
            }
        };

        $scope.sortByCuisine = function() {
            if($scope.sortIndex != 2) {
                $("#card-wrapper").isotope({sortBy: 'cuisine'});
                $scope.sortIndex = 2;
            }
        };

        $scope.sortByMeal = function() {
            if($scope.sortIndex != 3) {
                $("#card-wrapper").isotope({sortBy: 'meal'});
                $scope.sortIndex = 3;
            }
        };

        $scope.showSearchBar = function() {
            var search = $('#search');
            search.css({'visibility' : 'visible'});
            search.focus();
        };

        $scope.hideSearchBar = function() {
            var search = $('#search');
            search.css({'visibility' : 'hidden'});
        };
    }]);

})();
