//noinspection JSHint
var app = angular.module('lib', []);


app.controller('AuthorListCtrl',['$http','$rootScope','$timeout', function($http, $rootScope, $timeout, $sce){
    'use strict';
    $rootScope.currentAuthor={author:{avtorId:-1}};
    var authorListCtrl=this;
    $http.get('/data/authors/A/1').success(function(data){
        authorListCtrl.authors=data;
    }).error(function(data){
        authorListCtrl.authors=[
            {lastName:"ERROR"},
            {lastName:data}
        ];

    });

    this.setCurrentAuthor=function(avtorId){
        $http.get('/data/author/'+avtorId).success(function(data) {
            $rootScope.currentAuthor = data;
        });
    };

    this.isCurrentAuthor=function(avtorId){
        return $rootScope.currentAuthor.author.avtorId===avtorId;

    };

    authorListCtrl.pageNumber=1;
    authorListCtrl.authorFilter='A';
    this.filterFunc=function(){
        $timeout.cancel(authorListCtrl.filterPromise);
        authorListCtrl.filterPromise=$timeout(function(){
            $http.get('/data/authors/'+authorListCtrl.authorFilter+'/'+authorListCtrl.pageNumber).success(function(data) {
                authorListCtrl.authors = data;
            });
        },500);
    };

}]);
app.controller("AuthorCtrl",['$rootScope','$sce',function($rootScope, $sce){
    'use strict';
    this.author=$rootScope.currentAuthor.author;
    this.authorAnnotation=$rootScope.currentAuthor.authorAnnotation;
    this.books=$rootScope.currentAuthor.books;
    this.sequences=$rootScope.currentAuthor.sequences;
    var authorCtrl=this;

    this.to_trusted = function(html_code) {
        return $sce.trustAsHtml(html_code);
    };

    $rootScope.$watch(
        // This function returns the value being watched. It is called for each turn of the $digest loop
        function() {
            return $rootScope.currentAuthor;
        },
        // This is the change listener, called when the value returned from the above function changes
        function(newValue, oldValue) {
            if ( newValue !== oldValue ) {
                authorCtrl.author = newValue.author;
                authorCtrl.authorAnnotation=$rootScope.currentAuthor.authorAnnotation;
                authorCtrl.sequences=newValue.sequences;
                authorCtrl.books=newValue.books;
                authorCtrl.author.author = newValue.author;
            }
        }
    );
}]);


