<template>
    <div>
      <!-- 面包屑导航区 -->
      <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>权限管理</el-breadcrumb-item>
        <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
      </el-breadcrumb>

        <div style="padding: 10px 0">
            <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入名称" v-model="name"
                      clearable></el-input>
            <el-button style="margin-left: 3px" type="primary" @click="load">搜索</el-button>
            <el-button style="margin-left: 3px" type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 5px 0">
            <el-button type="primary" @click="handleAdd(null)">新增<i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" slot="reference" :disabled="delBtlStatus" @click="deleteBatch(null)">批量删除<i
                    class="el-icon-delete"></i></el-button>
        </div>

        <el-table :data="tableData" v-loading="loading" border stripe :header-cell-class-name="headerBg" row-key="id"
                  default-expand-all
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55 "/>
            <el-table-column prop="id" label="ID" width="80" sortable>
            </el-table-column>
            <el-table-column prop="name" label="名称">
            </el-table-column>
            <el-table-column prop="path" label="路径">
            </el-table-column>
            <el-table-column prop="pagePath" label="页面路径" width="220">
            </el-table-column>
            <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
                <template slot-scope="scope">
                    <i :class="scope.row.icon"></i>
                </template>
            </el-table-column>
            <el-table-column prop="description" label="描述">
            </el-table-column>
            <el-table-column prop="sortNum" label="顺序">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="300">
                <template slot-scope="scope">
                    <el-button type="primary" size="small" @click="handleAdd(scope.row.id)" v-if="!scope.row.path">新增子菜单<i
                            class="el-icon-plus"></i></el-button>
                    <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i>
                    </el-button>
                    <el-button type="danger" size="small" slot="reference" @click="handleDelete(scope.row.id)">删除<i
                            class="el-icon-delete"></i></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--弹窗-->
        <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" :model="form" ref="form" :rules="formRlues">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="路径" prop="path">
                    <el-input v-model="form.path" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="页面路径" prop="pagePath">
                    <el-input v-model="form.pagePath" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="图标" prop="icon">
                    <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
                        <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
                            <i :class="item.value"/>{{ item.name }}
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="form.description" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="顺序" prop="sortNum">
                    <el-input v-model="form.sortNum" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Menu",
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                name: '',
                form: {},
                formRlues: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'}
                    ],
                },
                multipleSelection: [],
                headerBg: 'headerBg',
                dialogFormVisible: false,
                options: [],
                delBtlStatus: true,
                loading: false
            }
        },
        //进入页面刷新数据
        created() {
            //请求分页查询数据
            this.load()
        },
        methods: {
            //分页查询
            load() {
                this.loading = true
                this.request.get("/menu", {
                    params: {
                        name: this.name,
                    }
                }).then(res => {
                    this.tableData = res.data
                });
                //请求图标的数据
                this.request.get("/dict/getAll").then(res => {
                    this.options = res.data
                    this.loading = false
                });
            },
            //搜索重置按钮
            reset() {
                this.name = ""
                this.load()
            },
            //新增
            save() {
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        this.request.post("/menu/save", this.form).then(res => {
                            console.log(res)
                            if (res.code === 200) {
                                this.$message({
                                    showClose: true,
                                    type: "success",
                                    message: "保存成功",
                                    duration: 1000
                                })
                                this.load()
                                this.dialogFormVisible = false
                            } else {
                                this.$message({
                                    showClose: true,
                                    type: "error",
                                    message: "保存失败",
                                    duration: 1000
                                })
                            }
                        })
                    }
                })
            },
            //打开新增弹窗
            handleAdd(pid) {
                this.dialogFormVisible = true
                this.form = {}
                if (pid) {
                    this.form.pid = pid
                }
            },
            //编辑
            handleEdit(row) {
                this.form = JSON.parse(JSON.stringify(row))  //深拷贝
                this.dialogFormVisible = true
                //请求图标的数据
                this.request.get("/dict/getAll").then(res => {
                    this.options = res.data
                });
            },
            //根据ID删除
            handleDelete(id) {
                console.log(id)
                this.$confirm('确定要删除此菜单吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.request.delete("/menu/delete/" + id).then(res => {
                        if (res.code === 200) {
                            this.$message({
                                showClose: true,
                                type: "success",
                                message: "删除成功",
                                duration: 1000
                            })
                            this.load()
                        } else {
                            this.$message({
                                showClose: true,
                                type: "error",
                                message: "删除失败",
                                duration: 1000
                            })
                            this.load()
                        }
                    })
                })
            },
            //批量删除 val:数组
            handleSelectionChange(val) {
                this.multipleSelection = val
                this.delBtlStatus = val.length === 0
            },
            deleteBatch(id) {
                let ids = []
                if (id) {
                    ids.push(id)
                } else {
                    this.multipleSelection.forEach(row => {
                        ids.push(row.id)
                    })
                }
                this.$confirm('确定要删除您选中的菜单吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.request.post("/menu/delete/batch", ids).then(res => {
                        if (res.code === 200) {
                            this.$message({
                                showClose: true,
                                type: "success",
                                message: "批量删除成功",
                                duration: 1000
                            })
                            this.load()
                        } else {
                            this.$message({
                                showClose: true,
                                type: "error",
                                message: "批量删除失败",
                                duration: 1000
                            })
                        }
                    })
                })
            },
        }
    }
</script>

<style>
    .headerBg {
        background: #eee !important;
    }

    .fontSize18 {
        font-size: 18px;
    }

    .fontSize12 {
        font-size: 12px;
    }

</style>