"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy) {
	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");
	var restVat = $restalchemy.init({ root: $config.apiroot }).at("vat");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
		});
	}

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
				loadExpenses();
			});
		}
	};

	$scope.clearExpense = function() {
		$scope.newExpense = {};
	};

    var loadVatRate = function() {
        restVat.get().then(function(vatRate) {
            $scope.vatRate = vatRate;
        });
    }

	$scope.calculateVat = function() {
	    if (!$scope.newExpense.amount) {
	        return "";
	    }
	    var amount = $scope.newExpense.amount.toUpperCase().trim();
	    var vat = "";
	    var currency = "EUR"
	    if (amount.endsWith(currency)) {
	        var euroIndex = amount.indexOf(currency);
	        amount = amount.substring(0, euroIndex).trim();
	    } else {
	        currency = "";
	    }
        if (!isNaN(amount)) {
            vat = Number(amount) * $scope.vatRate / (100 + $scope.vatRate);
        }

        return vat.toFixed(2) + " " + currency;
    };


	// Initialise scope variables
	loadVatRate();
	loadExpenses();
	$scope.clearExpense();
}]);

app.directive("dateValidation", function() {
    return {
        require: "ngModel",
        link: function(scope, element, attr, mCtrl) {
            function dateValidation(value) {
                mCtrl.$setValidity("validDate", moment(value, "DD/MM/YYYY", true).isValid());
                return value;
            }
            mCtrl.$parsers.push(dateValidation);
        }

    }
});
