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

    module.controller('MainController', ['$scope', '$timeout', '$http', function ($scope, $timeout, $http) {
        
        /* AP: Index 0 = tableView, Index 1 = cardView */
        $scope.tabIndex = 0;

        /* AP: Index 0 = sortByPopular, Index 1 = sortByName, Index 2 = sortByCuisine, Index 3 = sortByMeal */
        $scope.sortIndex = 0;
        
        $scope.searchQuery = "";

        $scope.listOfItems = [];
        $scope.mealtypes = [];
        
        $http.defaults.headers.common['Authorization'] = getAuthString();

        $scope.getListOfItems = function() {
            $http({
                url: getAPIURL() + "/items",
                method: "GET"
            }).success(function(data, status, headers, config) {
                if(data != null && data.items != undefined && data.items != null) {
                    $scope.listOfItems = data.items;

                    //$scope.$apply();

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
                alert("Something went wrong");
            });
        };

        $scope.getMealTypes = function() {
            $http({
                url: getAPIURL() + "/meal",
                method: "GET"
            }).success(function(data, status, headers, config) {
                if(data != null && data.mealtypes != undefined && data.mealtypes != null) {
                    $scope.mealtypes = data.mealtypes;

                    //$scope.$apply();
                }
            }).error(function (data, status, headers, config) {
                alert("Something went wrong here");
            });
        };
        
        $scope.getListOfItems();
        $scope.getMealTypes();

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
