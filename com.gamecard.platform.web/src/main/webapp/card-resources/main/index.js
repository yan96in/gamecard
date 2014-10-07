/// <reference path="jquery.js" />

$(document).ready(function () {

    //------------------------------
    // ������Ч��
    //---------------------------
    $(".applist li").hover(
		  function () {
		      $(this).addClass("cur");
		  },
		  function () {
		      $(this).removeClass("cur");
		  }
	  );

    //����������
    var ff = new SellerScroll();
    $('#scroll-list').bxCarousel({
        display_num: 5,
        move: 1,
        speed: 400,
        margin: 0,
        auto: true,
        auto_interval: 2000,
        auto_dir: 'next',
        auto_hover: true,
        next_text: 'next',
        next_image: '',
        prev_text: 'prev',
        prev_image: '',
        controls: true
    });

    //�õ�ƬЧ��
    $('#slider li').eq(0).show();
    var len = $('#num span').length;
    var index = 0;

    $('#num span').mouseover(function () {
        index = $('#num span').index(this);
        picShow(index);
    });
    $('#box').hover(function () {
        if (playPic) {
            clearInterval(playPic);
        }
    }, function () {
        playPic = setInterval(function () {
            picShow(index);
            index++;
            if (index == len) { index = 0 }
        }, 3000)
    });
    var playPic = setInterval(function () {
        picShow(index);
        index++;
        if (index == len) { index = 0 }
    }, 3000)
    function picShow(i) {
        $('#slider li').eq(i).stop(true, true).fadeIn().siblings().fadeOut();
        $('#num > span').eq(i).addClass('on').siblings().removeClass('on');
    }


});


//------------------------------
// ���ҹ���Ч�����
//---------------------------
var SellerScroll = function (options) {
    this.SetOptions(options);
    this.lButton = this.options.lButton;
    this.rButton = this.options.rButton;
    this.oList = this.options.oList;
    this.showSum = this.options.showSum;

    this.iList = $("#" + this.options.oList + " > li");
    this.iListSum = this.iList.length;
    this.iListWidth = this.iList.outerWidth(true);
    this.moveWidth = this.iListWidth * this.showSum;

    this.dividers = Math.ceil(this.iListSum / this.showSum); //����Ϊ���ٿ�
    this.moveMaxOffset = (this.dividers - 1) * this.moveWidth;
    this.LeftScroll();
    this.RightScroll();
};
SellerScroll.prototype = {
    SetOptions: function (options) {
        this.options = {
            lButton: "prev",
            rButton: "next",
            oList: "slide-pic ul",
            showSum: 6	//һ�ι������ٸ�items
        };
        $.extend(this.options, options || {});
    },
    ReturnLeft: function () {
        return isNaN(parseInt($("#" + this.oList).css("left"))) ? 0 : parseInt($("#" + this.oList).css("left"));
    },
    LeftScroll: function () {
        if (this.dividers == 1) return;
        var _this = this, currentOffset;
        $("#" + this.lButton).click(function () {
            currentOffset = _this.ReturnLeft();
            if (currentOffset == 0) {
                for (var i = 1; i <= _this.showSum; i++) {
                    $(_this.iList[_this.iListSum - i]).prependTo($("#" + _this.oList));
                }
                $("#" + _this.oList).css({ left: -_this.moveWidth });
                $("#" + _this.oList + ":not(:animated)").animate({ left: "+=" + _this.moveWidth }, { duration: "slow", complete: function () {
                    for (var j = _this.showSum + 1; j <= _this.iListSum; j++) {
                        $(_this.iList[_this.iListSum - j]).prependTo($("#" + _this.oList));
                    }
                    $("#" + _this.oList).css({ left: -_this.moveWidth * (_this.dividers - 1) });
                }
                });
            } else {
                $("#" + _this.oList + ":not(:animated)").animate({ left: "+=" + _this.moveWidth }, "slow");
            }
        });
    },
    RightScroll: function () {
        if (this.dividers == 1) return;
        var _this = this, currentOffset;
        $("#" + this.rButton).click(function () {
            currentOffset = _this.ReturnLeft();
            if (Math.abs(currentOffset) >= _this.moveMaxOffset) {
                for (var i = 0; i < _this.showSum; i++) {
                    $(_this.iList[i]).appendTo($("#" + _this.oList));
                }
                $("#" + _this.oList).css({ left: -_this.moveWidth * (_this.dividers - 2) });

                $("#" + _this.oList + ":not(:animated)").animate({ left: "-=" + _this.moveWidth }, { duration: "slow", complete: function () {
                    for (var j = _this.showSum; j < _this.iListSum; j++) {
                        $(_this.iList[j]).appendTo($("#" + _this.oList));
                    }
                    $("#" + _this.oList).css({ left: 0 });
                }
                });
            } else {
                $("#" + _this.oList + ":not(:animated)").animate({ left: "-=" + _this.moveWidth }, "slow");
            }
        });
    }
};
