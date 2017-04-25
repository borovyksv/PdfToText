webpackJsonp([1,4],{

/***/ 402:
/***/ (function(module, exports) {

function webpackEmptyContext(req) {
	throw new Error("Cannot find module '" + req + "'.");
}
webpackEmptyContext.keys = function() { return []; };
webpackEmptyContext.resolve = webpackEmptyContext;
module.exports = webpackEmptyContext;
webpackEmptyContext.id = 402;


/***/ }),

/***/ 403:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser_dynamic__ = __webpack_require__(490);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__environments_environment__ = __webpack_require__(513);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__app_app_module__ = __webpack_require__(511);




if (__WEBPACK_IMPORTED_MODULE_2__environments_environment__["a" /* environment */].production) {
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["a" /* enableProdMode */])();
}
__webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_3__app_app_module__["a" /* AppModule */]);
//# sourceMappingURL=C:/Users/user-pc/Desktop/PdfToText/src/main.js.map

/***/ }),

/***/ 510:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__documents_service__ = __webpack_require__(512);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_Rx__ = __webpack_require__(670);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_Rx___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_Rx__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
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
    function AppComponent(documentService) {
        var _this = this;
        this.documentService = documentService;
        __WEBPACK_IMPORTED_MODULE_2_rxjs_Rx__["Observable"].interval(2000).subscribe(function (x) {
            _this.documentService.getDocumentsProgress().subscribe(function (documents) {
                return _this.documents = documents;
            });
            _this.documentService.getConvertedDocuments().subscribe(function (converted) {
                return _this.converted = converted;
            });
        });
    }
    AppComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["U" /* Component */])({
            selector: 'app-root',
            template: __webpack_require__(668),
            styles: [__webpack_require__(667)],
            providers: [__WEBPACK_IMPORTED_MODULE_1__documents_service__["a" /* DocumentService */]]
        }), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__documents_service__["a" /* DocumentService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_1__documents_service__["a" /* DocumentService */]) === 'function' && _a) || Object])
    ], AppComponent);
    return AppComponent;
    var _a;
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
//# sourceMappingURL=C:/Users/user-pc/Desktop/PdfToText/src/app.component.js.map

/***/ }),

/***/ 511:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__(217);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_forms__ = __webpack_require__(481);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_http__ = __webpack_require__(315);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__app_component__ = __webpack_require__(510);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var AppModule = (function () {
    function AppModule() {
    }
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
        }), 
        __metadata('design:paramtypes', [])
    ], AppModule);
    return AppModule;
}());
//# sourceMappingURL=C:/Users/user-pc/Desktop/PdfToText/src/app.module.js.map

/***/ }),

/***/ 512:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__(315);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__ = __webpack_require__(373);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__);
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
        return this.http.get('http://192.168.99.100:8080/documents/progress')
            .map(function (res) { return res.json(); });
    };
    DocumentService.prototype.getConvertedDocuments = function () {
        return this.http.get('http://192.168.99.100:8080/documents/converted')
            .map(function (res) { return res.json(); });
    };
    DocumentService = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["d" /* Injectable */])(), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */]) === 'function' && _a) || Object])
    ], DocumentService);
    return DocumentService;
    var _a;
}());
//# sourceMappingURL=C:/Users/user-pc/Desktop/PdfToText/src/documents.service.js.map

/***/ }),

/***/ 513:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `angular-cli.json`.
var environment = {
    production: false
};
//# sourceMappingURL=C:/Users/user-pc/Desktop/PdfToText/src/environment.js.map

/***/ }),

/***/ 667:
/***/ (function(module, exports) {

module.exports = "div.progress{\r\n  position: relative;\r\n  margin-top:1px;\r\n  margin-bottom:0px;\r\n  height:19px;\r\n}\r\n\r\ndiv.progress span {\r\n  position: absolute;\r\n  display: block;\r\n  width: 100%;\r\n  color: black;\r\n}\r\n"

/***/ }),

/***/ 668:
/***/ (function(module, exports) {

module.exports = "<nav class=\"navbar navbar-default\">\n  <div class=\"container\">\n    <div class=\"navbar-header\"><a class=\"navbar-brand navbar-link\" href=\"#\"><i class=\"fa fa-file-pdf-o\"></i> PDF\n      Dashboard</a>\n      <button class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navcol-1\"><span class=\"sr-only\">Toggle navigation</span><span\n        class=\"icon-bar\"></span>\n        <span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button>\n    </div>\n    <div class=\"collapse navbar-collapse\" id=\"navcol-1\">\n      <ul class=\"nav navbar-nav navbar-right\">\n        <li role=\"presentation\"><a href=\"#\">Item</a></li>\n      </ul>\n    </div>\n  </div>\n</nav>\n<div class=\"container\">\n  <div class=\"row\">\n    <div class=\"col-md-4\">\n      <div class=\"page-header\">\n        <h4>Converted Docs:</h4></div>\n\n      <div *ngFor=\"let con of converted\">\n        <p>{{con.name}} :\n          {{con.numberOfPages}} pages</p>\n      </div>\n    </div>\n    <div class=\"col-md-8\">\n      <div class=\"page-header\">\n        <h4>Documents in process </h4></div>\n      <div class=\"row\">\n        <div *ngFor=\"let document of documents\">\n          <div class=\"col-md-6\">\n            <div class=\"panel panel-default\">\n\n              <div class=\"panel-heading\">\n                <h3 class=\"panel-title\">{{document.docName}} progress:</h3></div>\n\n              <div class=\"panel-body\">\n                <div class=\"row\">\n                  <div class=\"col-md-3\">\n                    <p>Pages</p>\n                  </div>\n                  <div class=\"col-md-9\">\n                    <div class=\"progress\">\n                      <div class=\"progress-bar progress-bar-info\" aria-valuenow=\"document.pagesProgress\"\n                           aria-valuemin=\"0\" aria-valuemax=\"100\" [style.width]=\"document.pagesProgress + '%'\"><span>{{document.pagesProgress}}%</span>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"col-md-3\">\n                    <p>Images</p>\n                  </div>\n                  <div class=\"col-md-9\">\n                    <div class=\"progress\">\n                      <div class=\"progress-bar progress-bar-success\" aria-valuenow=\"document.imagesProgress\"\n                           aria-valuemin=\"0\" aria-valuemax=\"100\" [style.width]=\"document.imagesProgress + '%'\"><span>{{document.imagesProgress}}%</span>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"col-md-3\">\n                    <p>Text</p>\n                  </div>\n                  <div class=\"col-md-9\">\n                    <div class=\"progress\">\n                      <div class=\"progress-bar\" aria-valuenow=\"document.textProgress\" aria-valuemin=\"0\"\n                           aria-valuemax=\"100\" [style.width]=\"document.textProgress + '%'\"><span>{{document.textProgress}}%</span>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n                <div class=\"row\">\n                  <div class=\"col-md-12\">\n                    <div ng-show=\"document.errors!=null\"><a class=\"btn btn-danger btn-block btn-xs collapsed\" data-toggle=\"collapse\"\n                            aria-expanded=\"false\" aria-controls=\"collapse-1\" role=\"button\"\n                            href=\"#collapse-1\">Errors </a>\n                      <div class=\"collapse\" id=\"collapse-1\" aria-expanded=\"false\" style=\"height: 0px;\">\n                        <p>{{document.errors}}</p>\n                      </div>\n                    </div>\n                  </div>\n                </div>\n              </div>\n            </div>\n          </div>\n        </div>\n      </div>\n    </div>\n  </div>\n</div>\n"

/***/ }),

/***/ 943:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(403);


/***/ })

},[943]);
//# sourceMappingURL=main.bundle.map