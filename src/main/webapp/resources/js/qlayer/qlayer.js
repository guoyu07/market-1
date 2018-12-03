if (typeof qlayer == "undefined") {
    qlayer = {};
}
qlayer = {

    init: function (param) {
        var _defaultParam = {
            "clickId": "",
            "showType": "html",  //html , iframe
            "showDivId": "",
            "showUrl": "",
            "title": "",
            "closeButtonFlag": true,
            "closeFn": false,
            "okButtonFlag": false,
            "okButtonName": "OK",
            "okFn": false,
            "cancelButtonFlag": false,
            "cancelButtonName": "Cancel",
            "cancelFn": false,
            "height": "350",
            "width": "740",
            "top": ""
        };

        param = $.extend(_defaultParam, param);
        var _p = param, _self = qlayer , _body = $("body");
        _self.createQlayer(_p, _self).appendTo(_body);
        _self.maskEvent(_self, _p, function () {
            if ("html" == _p.showType) {
                _self.showHtmlFn(_p.showDivId);
            } else if ("iframe" == _p.showType) {
                _self.showIframeFn(_p.showUrl, _p.width - 30, _p.height);
            }
        });


    },


    createQlayer: function (param, self) {
        var _qlayer = $("#qlayerTop");
        if (_qlayer.length > 0)_qlayer.remove();


        var _divOut = $("<div></div>").attr({"id": "qlayerTop"}).addClass("dialogBox").css("width", param.width);

        var _divDialog = $("<div></div>").addClass("dialog");
        var _divDialogTitle = $("<div></div>").addClass("title").html(param.title);
        var _divDialogClose = $("<a></a>").addClass("close-tip-icon").attr({"href": "###"}).unbind("click.ql").bind("click.ql", function () {
            self.closeMask();
            self.addEvent("closeFn", param.closeFn);
        });
        var _divDialogContent = $("<div></div>").addClass("content");
        var _divDialogContent1 = $("<div></div>");
        var _divDialogContent2 = $("<div></div>").attr({"id": "dialogContent", "align": "center"});

        _divDialogContent1.append(_divDialogContent2);
        _divDialogContent.append(_divDialogContent1);

        var _divDialogOperation = $("<div></div>").addClass("operation");
        var _divDialogButtonOK = $("<button>" + param.okButtonName + "</button>").attr({"class": "btn  btnGreen", "id": "dialogOK"});
        var _divDialogButtonCancel = $("<button>" + param.cancelButtonName + "</button>").attr({"class": "btn btnGray", "id": "dialogCancel"});

        var _bottomDivFlag = false;
        if (true == param.okButtonFlag) {
            _divDialogButtonOK.unbind("click.ql").bind("click.ql", function () {
                var _okFnReturn = self.addEvent("okFn", param.okFn);
                if (_okFnReturn != false) {
                    self.closeMask();
                }
            });
            _divDialogOperation.append(_divDialogButtonOK);
            _bottomDivFlag = true;
        }

        if (true == param.cancelButtonFlag) {
            _divDialogButtonCancel.unbind("click.ql").bind("click.ql", function () {
                var _cancelFnReturn = self.addEvent("cancelFn", param.cancelFn);
                if (_cancelFnReturn != false) {
                    self.closeMask();
                }
            });
            _divDialogOperation.append(_divDialogButtonCancel);
            _bottomDivFlag = true;
        }
        _divDialog.append(_divDialogTitle);
        if (param.closeButtonFlag) _divDialog.append(_divDialogClose);
        _divDialog.append(_divDialogContent);
        if (_bottomDivFlag) _divDialog.append(_divDialogOperation);

        _divOut.append(_divDialog);
        return _divOut;
    },


    showHtmlFn: function (divId) {
        var _content = $("#dialogContent");
        _content.empty();
        var _sourceDiv = $("#" + divId);
        var _cloneDiv = _sourceDiv.clone(true).attr({"id": "qlayer_" + divId});

        $.each(_cloneDiv.find("*"), function () {
            if (typeof $(this).attr("id") != "undefined") {
                var _id = $(this).attr("id");
                $(this).attr("id", "qlayer_" + _id);
            }
        });
        _cloneDiv.show();
        _cloneDiv.appendTo(_content);
    },


    showIframeFn: function (href, width, height) {
        var _dc = $("#dialogContent");
        _dc.empty();
        var _imgLoad = $("<div></div>").attr({"id": "imgLoad"}).addClass("imgload");
        var _iframe = $("<iframe></iframe>").attr({"id": "frameInfo", "width": width, "height": height, "name": "frameInfo", "src": href, "frameborder": "0"}).load(function () {
            $("#imgLoad").hide();
        });
        _dc.append(_imgLoad).append(_iframe);

    },


    maskEvent: function (self, options, showFn) {
        if ($("#lean_overlay").length <= 0) {
            var _overlay = $("<div id='lean_overlay'></div>");
            $("body").append(_overlay);
        }

        var _defaults = {"top": 50, "overlay": 0.5};
        options = $.extend(_defaults, options);
        var _o = options;
        if (_o.clickId == "") {
            self.showMask(_o, showFn);
        } else {
            var _clickObj = null;

            if (_o.clickId.indexOf(".") == 0) {
                _clickObj = $(_o.clickId);
            } else {
                _clickObj = $("#" + _o.clickId);
            }


            _clickObj.unbind("click.ql").bind("click.ql", function () {
                self.showMask(_o, showFn);
            });
        }

    },

    showMask: function (param, showFn) {
        var _modelObj = $("#qlayerTop");
        var _maskDivObj = $("#lean_overlay");

        var _model_height = _modelObj.outerHeight();
        var _model_width = _modelObj.outerWidth();


        if (_model_height < 500)_model_height = 500;
        if (param.top != "") {
            _model_height = param.top;
        }

        _maskDivObj.css({"display": "block", opacity: 0});
        _maskDivObj.fadeTo(200, param.overlay);
        _modelObj.css({"display": "block", "position": "fixed", "opacity": 0, "z-index": 11000, "left": 50
            + "%", "margin-left": -(_model_width / 2) + "px", "top": 50 + "%", "margin-top": -(_model_height / 2) + "px"});
        _modelObj.fadeTo(200, 1);
        showFn();
    },


    closeMask: function () {
        $("#lean_overlay").fadeOut(200);
        $("#qlayerTop").css({"display": "none"});
    },


    addEvent: function (event, callback) {
        $.event.trigger(event);
        if (callback) {
            return callback.call();
        }
    }

};
