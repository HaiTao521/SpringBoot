layui.define(['jquery'], function (exports) {

    var $ = layui.$;

    var $Viewerjs = function(id) {
        this.id = id;
        this.options = null;
    };

    $Viewerjs.prototype = {
        /**
         * 初始化viewerjs的设置
         */
        initOptions : function() {
            var options = {
            	inline:true
            };
            return options;
        },

        /**
         * 手动设置viewerjs的设置
         */
        setOptions : function(val) {
            this.options = val;
        },

        /**
         * 初始化viewerjs
         */
        init : function() {
            var temp_viewer_options = null;
            if(this.options != null){
            	temp_viewer_options = this.options;
            }else{
            	temp_viewer_options = this.initOptions();
            }
           $("#"+this.id).viewer(temp_viewer_options);
        },
        
        getViewer: function(){
        	return $("#"+this.id).data('viewer');
        }
    };

    exports('viewerjs', $Viewerjs);

});