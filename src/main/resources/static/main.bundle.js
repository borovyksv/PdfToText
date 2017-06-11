webpackJsonp([1],{

/***/ "../../../../../src async recursive":
/***/ (function(module, exports) {

function webpackEmptyContext(req) {
	throw new Error("Cannot find module '" + req + "'.");
}
webpackEmptyContext.keys = function() { return []; };
webpackEmptyContext.resolve = webpackEmptyContext;
module.exports = webpackEmptyContext;
webpackEmptyContext.id = "../../../../../src async recursive";

/***/ }),

/***/ "../../../../../src/app/app.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "div.progress{\r\n  position: relative;\r\n  margin-top:1px;\r\n  margin-bottom:0px;\r\n  height:19px;\r\n}\r\n\r\ndiv.progress span {\r\n  position: absolute;\r\n  display: block;\r\n  width: 100%;\r\n  color: black;\r\n}\r\n\r\n.selectors {\r\n  margin-top: 3em;\r\n}\r\n\r\n.upload-btn {\r\n  text-align: center;\r\n  margin-top: 1em;\r\n}\r\n.file-input{\r\n  margin-top: 1em;\r\n}\r\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/app.component.html":
/***/ (function(module, exports) {

module.exports = "<nav class=\"navbar navbar-default\">\n  <div class=\"container\">\n    <div class=\"navbar-header\"><a class=\"navbar-brand navbar-link\" href=\"#\"><i class=\"fa fa-file-pdf-o\"></i> PDF\n      Dashboard</a>\n      <button class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navcol-1\"><span class=\"sr-only\">Toggle navigation</span><span\n        class=\"icon-bar\"></span>\n        <span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button>\n    </div>\n    <div class=\"collapse navbar-collapse\" id=\"navcol-1\">\n      <ul class=\"nav navbar-nav navbar-right\">\n        <!--<li role=\"presentation\"><a href=\"#\">Item</a></li>-->\n      </ul>\n    </div>\n  </div>\n</nav>\n\n\n<div class=\"container\">\n  <h5>Upload PDF</h5>\n  <form (ngSubmit)=\"uploadPdf()\" class=\"bootstrap-form-wsith-validation\">\n    <div class=\"col-md-4\">\n      <div class=\"form-group\">\n        <div class=\"input-field col s12\">\n          <input id=\"Vendor\" type=\"text\" name=\"Vendor\" class=\"validate filter-input \" [(ngModel)]=vehicleQuery\n                 (keyup)=filterVehicles()>\n          <label for=\"Vendor\">Vendor</label>\n        </div>\n        <div class=\"suggestions\" *ngIf=\"vehicleFilteredList.length > 0\">\n          <ul *ngFor=\"let item of vehicleFilteredList\">\n            <li>\n              <a (click)=\"selectVehicle(item.vendor)\">{{item.vendor}}</a>\n            </li>\n          </ul>\n        </div>\n      </div>\n      <div class=\"form-group\">\n        <div class=\"input-field col s12\">\n          <input id=\"Model\" type=\"text\" name=\"Model\" class=\"validate filter-input \" [(ngModel)]=modelQuery\n                 (keyup)=filterModels()>\n          <label for=\"Model\">Model</label>\n        </div>\n        <div class=\"suggestions\" *ngIf=\"modelFilteredList.length > 0\">\n          <ul *ngFor=\"let item of modelFilteredList\">\n            <li>\n              <a (click)=\"selectModel(item)\">{{item}}</a>\n            </li>\n          </ul>\n        </div>\n      </div>\n    </div>\n\n    <div class=\"col-md-4 selectors\">\n\n      <select class=\"form-control input-field col s12\" id=\"years\" (change)=\"changeSelectedYear($event.target.value)\"\n              required>\n        <option value=\"\" disabled selected>Year</option>\n        <option *ngFor=\"let year of years\" [value]=\"year\">{{year}}</option>\n      </select>\n      <select class=\"form-control input-field col s12\" id=\"types\" (change)=\"changeSelectedType($event.target.value)\"\n              required>\n        <option value=\"\" disabled selected>Document type</option>\n        <option *ngFor=\"let type of types\" [value]=\"type\">{{type}}</option>\n      </select>\n    </div>\n\n    <div class=\"form-group col-md-4\">\n      <div class=\"input-field col s12\">\n        <input id=\"Options\" type=\"text\" name=\"Options\" class=\"validate filter-input \" [(ngModel)]=options>\n        <label for=\"Options\">option 1, option 2, option 3</label>\n      </div>\n      <div class=\"form-group file-input\">\n        <label class=\"control-label\" for=\"file-input\">File Input</label>\n        <input type=\"file\" name=\"file-input\" id=\"file-input\"\n               (change)=\"fileChange($event)\" placeholder=\"Upload file\" accept=\".pdf\">\n      </div>\n      <!--vehicleQuery=-->\n      <!--{{ vehicleQuery}}-->\n      <!--modelQuery=-->\n      <!--{{ modelQuery}}-->\n      <!--selectedYear=-->\n      <!--{{ selectedYear}}-->\n      <!--selectedType=-->\n      <!--{{ selectedType}}-->\n      <!--options=-->\n      <!--{{ options}}-->\n      <div class=\"form-group upload-btn\">\n        <button class=\"btn btn-primary\" type=\"submit\"\n                [disabled]=\"vehicleQuery==''||modelQuery==''||selectedType==''||fileList==null\">\n          Upload</button>\n      </div>\n    </div>\n\n  </form>\n</div>\n\n\n<div class=\"container\">\n  <div class=\"row\">\n    <div class=\"col-md-4\">\n      <div class=\"page-header\">\n        <h4>Converted Docs:</h4></div>\n\n      <div *ngFor=\"let con of converted\">\n        <p>{{con.name}} :\n          {{con.numberOfPages}} pages</p>\n      </div>\n    </div>\n    <div class=\"col-md-8\">\n      <div class=\"page-header\">\n        <h4>Documents in process </h4></div>\n      <div class=\"row\">\n        <div *ngFor=\"let document of documents\">\n          <div class=\"col-md-6\">\n            <div class=\"panel panel-default\">\n\n              <div class=\"panel-heading\">\n                <h3 class=\"panel-title\">{{document.docName}} progress:</h3></div>\n\n              <div class=\"panel-body\">\n                <div class=\"row\">\n                  <div class=\"col-md-3\">\n                    <p>Pages</p>\n                  </div>\n                  <div class=\"col-md-9\">\n                    <div class=\"progress\">\n                      <div class=\"progress-bar progress-bar-info\" aria-valuenow=\"document.pagesProgress\"\n                           aria-valuemin=\"0\" aria-valuemax=\"100\" [style.width]=\"document.pagesProgress + '%'\"><span>{{document.pagesProgress}}%</span>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"col-md-3\">\n                    <p>Images</p>\n                  </div>\n                  <div class=\"col-md-9\">\n                    <div class=\"progress\">\n                      <div class=\"progress-bar progress-bar-success\" aria-valuenow=\"document.imagesProgress\"\n                           aria-valuemin=\"0\" aria-valuemax=\"100\" [style.width]=\"document.imagesProgress + '%'\"><span>{{document.imagesProgress}}%</span>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"col-md-3\">\n                    <p>Text</p>\n                  </div>\n                  <div class=\"col-md-9\">\n                    <div class=\"progress\">\n                      <div class=\"progress-bar\" aria-valuenow=\"document.textProgress\" aria-valuemin=\"0\"\n                           aria-valuemax=\"100\" [style.width]=\"document.textProgress + '%'\"><span>{{document.textProgress}}%</span>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"col-md-12\">\n                    <div ng-show=\"document.errors!=null\"><a class=\"btn btn-danger btn-block btn-xs collapsed\"\n                                                            data-toggle=\"collapse\"\n                                                            aria-expanded=\"false\" aria-controls=\"collapse-1\"\n                                                            role=\"button\"\n                                                            href=\"#collapse-1\">Errors </a>\n                      <div class=\"collapse\" id=\"collapse-1\" aria-expanded=\"false\" style=\"height: 0px;\">\n                        <p>{{document.errors}}</p>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n              </div>\n            </div>\n          </div>\n        </div>\n      </div>\n    </div>\n  </div>\n</div>\n"

/***/ }),

/***/ "../../../../../src/app/app.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__documents_service__ = __webpack_require__("../../../../../src/app/documents.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_Observable__ = __webpack_require__("../../../../rxjs/Observable.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_Observable___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_Observable__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_observable_interval__ = __webpack_require__("../../../../rxjs/add/observable/interval.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_observable_interval___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_rxjs_add_observable_interval__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_rxjs_add_operator_toPromise__ = __webpack_require__("../../../../rxjs/add/operator/toPromise.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5_rxjs_add_operator_toPromise__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
/* unused harmony export Form */
/* unused harmony export Vehicle */
/* unused harmony export Document */
/* unused harmony export Converted */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






var AppComponent = (function () {
    // end upload a file
    function AppComponent(http, documentService) {
        var _this = this;
        this.http = http;
        this.documentService = documentService;
        // Form
        this.options = '';
        this.years = [2018];
        this.types = ['owner manual',
            'repair',
            'miscellaneous'];
        this.selectedType = '';
        // autocomplete
        // - vehicle
        this.vehicleQuery = '';
        this.vehicleFilteredList = [];
        // - model
        this.modelQuery = '';
        this.modelFilteredList = [];
        this.selectedVehicle = new Vehicle("", [""]);
        this.models = [];
        //noinspection TypeScriptUnresolvedFunction
        __WEBPACK_IMPORTED_MODULE_2_rxjs_Observable__["Observable"].interval(2000).subscribe(function (x) {
            _this.documentService.getDocumentsProgress().subscribe(function (documents) {
                return _this.documents = documents;
            });
            _this.documentService.getConvertedDocuments().subscribe(function (converted) {
                return _this.converted = converted;
            });
        });
    }
    AppComponent.prototype.changeSelectedYear = function (value) {
        this.selectedYear = value;
    };
    AppComponent.prototype.changeSelectedType = function (value) {
        this.selectedType = value;
    };
    AppComponent.prototype.filterVehicles = function () {
        if (this.vehicleQuery !== "") {
            this.vehicleFilteredList = this.vehicles.filter(function (el) {
                return el.vendor.toLowerCase().indexOf(this.vehicleQuery.toLowerCase()) > -1;
            }.bind(this));
        }
        else {
            this.vehicleFilteredList = [];
        }
    };
    AppComponent.prototype.selectVehicle = function (item) {
        this.vehicleQuery = item;
        this.vehicleFilteredList = [];
        this.changeSelectedVehicle(item);
    };
    AppComponent.prototype.filterModels = function () {
        if (this.modelQuery !== "") {
            this.modelFilteredList = this.models.filter(function (el) {
                return el.toLowerCase().indexOf(this.modelQuery.toLowerCase()) > -1;
            }.bind(this));
        }
        else {
            this.modelFilteredList = [];
        }
    };
    AppComponent.prototype.selectModel = function (item) {
        this.modelQuery = item;
        this.modelFilteredList = [];
    };
    AppComponent.prototype.changeSelectedVehicle = function (value) {
        for (var i in this.vehicles) {
            if (this.vehicles[i].vendor == value) {
                this.selectedVehicle = this.vehicles[i];
                this.models = this.vehicles[i].models;
                break;
            }
        }
    };
    AppComponent.prototype.fileChange = function (event) {
        this.fileList = event.target.files;
    };
    // get diagnostic() { return JSON.stringify(this.fileList); }
    AppComponent.prototype.uploadPdf = function () {
        console.log("upload PDF");
        if (this.fileList.length > 0) {
            var file = this.fileList[0];
            var formData = new FormData();
            var info = new Form(this.vehicleQuery, [this.modelQuery], [this.selectedYear], this.selectedType, [this.options]);
            // todo:remove
            console.log(info);
            formData.append('file', file, file.name);
            formData.append('info', new Blob([JSON.stringify(info)], {
                type: "application/json"
            }));
            var headers = new __WEBPACK_IMPORTED_MODULE_3__angular_http__["b" /* Headers */]();
            headers.append('Accept', 'application/json');
            var options = new __WEBPACK_IMPORTED_MODULE_3__angular_http__["c" /* RequestOptions */]({ headers: headers });
            this.http.post("http://localhost:8080/documents/store", formData, options)
                .toPromise()
                .then(function (res) { return console.log(res.json()); })
                .catch(function (error) { return console.log(error); });
        }
        this.vehicleQuery = '';
        this.modelQuery = '';
        this.selectedYear = null;
        this.selectedType = '';
        this.options = '';
        this.fileList = null;
    };
    AppComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.documentService.getVehicles().then(function (vehicles) {
            _this.vehicles = vehicles;
            _this.selectedVehicle = vehicles[0];
        });
        this.initYears();
    };
    AppComponent.prototype.initYears = function () {
        for (var i = this.years[0] - 1; i >= 1800; i--) {
            this.years.push(i);
        }
    };
    return AppComponent;
}());
AppComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_4" /* Component */])({
        selector: 'app-root',
        template: __webpack_require__("../../../../../src/app/app.component.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__documents_service__["a" /* DocumentService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_3__angular_http__["d" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_http__["d" /* Http */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__documents_service__["a" /* DocumentService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__documents_service__["a" /* DocumentService */]) === "function" && _b || Object])
], AppComponent);

var Form = (function () {
    function Form(vendor, model, year, type, options) {
        this.vendor = vendor;
        this.model = model;
        this.year = year;
        this.type = type;
        this.options = options;
    }
    return Form;
}());

var Vehicle = (function () {
    function Vehicle(vendor, models) {
        this.vendor = vendor;
        this.models = models;
    }
    return Vehicle;
}());

var Document = (function () {
    function Document() {
    }
    return Document;
}());

var Converted = (function () {
    function Converted() {
    }
    return Converted;
}());

var _a, _b;
//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ "../../../../../src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__("../../../platform-browser/@angular/platform-browser.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_forms__ = __webpack_require__("../../../forms/@angular/forms.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__app_component__ = __webpack_require__("../../../../../src/app/app.component.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["b" /* NgModule */])({
        declarations: [
            __WEBPACK_IMPORTED_MODULE_4__app_component__["a" /* AppComponent */]
        ],
        imports: [
            __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
            __WEBPACK_IMPORTED_MODULE_2__angular_forms__["a" /* FormsModule */],
            __WEBPACK_IMPORTED_MODULE_3__angular_http__["a" /* HttpModule */]
        ],
        providers: [],
        bootstrap: [__WEBPACK_IMPORTED_MODULE_4__app_component__["a" /* AppComponent */]]
    })
], AppModule);

//# sourceMappingURL=app.module.js.map

/***/ }),

/***/ "../../../../../src/app/documents.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__ = __webpack_require__("../../../../rxjs/add/operator/map.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_toPromise__ = __webpack_require__("../../../../rxjs/add/operator/toPromise.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_toPromise__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DocumentService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var DocumentService = (function () {
    function DocumentService(http) {
        this.http = http;
        console.log('Document service initialized...');
    }
    DocumentService.prototype.getDocumentsProgress = function () {
        return this.http.get('http://localhost:8080/documents/progress')
            .map(function (res) { return res.json(); });
    };
    DocumentService.prototype.getConvertedDocuments = function () {
        return this.http.get('http://localhost:8080/documents/converted')
            .map(function (res) { return res.json(); });
    };
    DocumentService.prototype.getVehicles = function () {
        return this.http.get('http://localhost:8080/vehicles')
            .toPromise()
            .then(function (response) { return response.json(); })
            .catch(this.handleError);
    };
    DocumentService.prototype.handleError = function (error) {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    };
    return DocumentService;
}());
DocumentService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["c" /* Injectable */])(),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* Http */]) === "function" && _a || Object])
], DocumentService);

var _a;
//# sourceMappingURL=documents.service.js.map

/***/ }),

/***/ "../../../../../src/environments/environment.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
// The file contents for the current environment will overwrite these during build.
var environment = {
    production: false
};
//# sourceMappingURL=environment.js.map

/***/ }),

/***/ "../../../../../src/main.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__("../../../platform-browser-dynamic/@angular/platform-browser-dynamic.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__("../../../../../src/app/app.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__("../../../../../src/environments/environment.ts");




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["a" /* enableProdMode */])();
}
__webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */]);
//# sourceMappingURL=main.js.map

/***/ }),

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("../../../../../src/main.ts");


/***/ })

},[0]);
//# sourceMappingURL=main.bundle.js.map