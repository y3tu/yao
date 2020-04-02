/**
 * 通用分页查询的实现
 */

export default {
    data() {
        return {
            pageUrl: '',//数据请求地址
            pageLoading: false,//分页表格loading
            page: {
                records: [],//表格中的数据
                total: 0,//数据总数
                current: 1,//当前页数
                size: 10,//默认每页数量
                orders: [],//升序降序字段
                entity: {},//请求实体筛选参数
                params: {}//请求筛选参数
            },
            time: 100
        }
    },
    methods: {
        /**
         * 后台查询分页数据
         */
        async findPage() {
            const _this = this;
            if (!await _this.findPageInit()) {
                return
            }
            return new Promise((resolve, reject) => {
                    _this.pageLoading = true;

                    //剔除无效参数
                    Object.keys(_this.page.entity).forEach(function (key) {
                        if (_this.page.entity[key] == null || _this.page.entity[key] === '') {
                            delete _this.page.entity[key];
                        }
                    });

                    //剔除无效参数
                    Object.keys(_this.page.params).forEach(function (key) {
                        if (_this.page.params[key] == null || _this.page.params[key] === '') {
                            delete _this.page.params[key];
                        }
                    });

                    //查询前先清空数据
                    _this.page.records = [];
                    _this.$page(_this.pageUrl, _this.page).then(res => {
                        _this.page = res.data;
                        setTimeout(() => {
                            _this.pageLoading = false
                        }, _this.time);
                        resolve(res)
                    }).catch(err => {
                        _this.pageLoading = false;
                        reject(err)
                    })
                }
            )
        },
        //查询前的准备工作 比如说对查询参数赋值或特殊处理
        findPageInit() {
            return true
        },
        //换页
        pageChange(e) {
            this.page.current = e;
            this.findPage()
        },
        //改变每页显示数量
        sizeChange(e) {
            this.page.current = 0;
            this.page.size = e;
            this.findPage()
        },
        //排序
        sortChange(val) {
            let field = val.prop;
            let order = val.order;
            this.page.orders = [];
            this.page.orders = [{
                column: field,
                asc: order === 'ascending'
            }];
            this.findPage();
        },
        //清空查询条件
        resetPage() {
            this.page.entity = {};
            this.page.params = {};
            this.page.orders = [];
        }

    }
}