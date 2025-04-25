<template>
    <div>
        <el-card :style="{ fontSize: '20px' }" style="height: 100px; padding: 0;">
            <el-row style="margin: 12px; padding: 0;">
                <!-- <el-col :span="3" style="font-size:18px">
                  食<br>物<br>表
                </el-col> -->
                <el-col :span="21">
                    <el-image :src="foodBG1" style="width: 450px; padding: 0;" />
                </el-col>
            </el-row>
        </el-card>
        <br>
        <el-table ref="foodTable" size="large" :style="{ fontSize: '18px' }" :data="foods" style="width: 100%;" max-height="600" class="table"
            header-row-class-name="table-header" :row-class-name="tableRowClassName">
            <el-table-column fixed prop="foodName" label="食物名称" width="187">
                <template #default="scope">
                    <div v-if="scope.row.isEditing">
                        <el-input v-model="scope.row.foodName" @blur="validateFoodName(scope.row)"
                            placeholder="请输入食物名称" />
                    </div>
                    <div v-else>
                        {{ scope.row.foodName }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="calorie" label="食物热量(kCal/100g)" width="auto">
                <template #default="scope">
                    <div v-if="scope.row.isEditing">
                        <el-input v-model.number="scope.row.calorie" @blur="validateCalorie(scope.row)"
                            placeholder="请输入卡路里" type="number" />
                    </div>
                    <div v-else>
                        {{ scope.row.calorie }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <div v-if="scope.row.isEditing">
                        <el-button :style="{ fontSize: '16px ' }" type="success" @click="saveEdit(scope.$index)">
                            保存
                        </el-button>
                        <el-button :style="{ fontSize: '16px ' }" @click="cancelEdit(scope.$index)">
                            取消
                        </el-button>
                    </div>
                    <div v-else>
                        <el-button :style="{ fontSize: '16px ' }" @click="handleEdit(scope.$index)">
                            编辑
                        </el-button>
                        <el-button :style="{ fontSize: '16px ' }" type="danger" @click="handleDelete(scope.$index)">
                            删除
                        </el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <el-button :style="{ fontSize: '18px' }" size="large" class="mt-4" style="width: auto" @click="onAddItem" text bg>
            <el-icon>
                <CirclePlusFilled />
            </el-icon>
            添加食物
        </el-button>
    </div>
</template>

<script lang="ts">
import { defineComponent, nextTick } from 'vue';
import { ElNotification } from 'element-plus';
import foodBG1 from '../assets/images/foodBG1.jpg';
import { mealPlanApi } from '../api/services';

// 定义食物接口
interface Food {
    foodName: string;
    calorie: number;
    isEditing?: boolean;
}

// 定义表格引用接口
interface TableRef {
    $el: HTMLElement;
}

export default defineComponent({
    name: 'addFood',
    data() {
        return {
            foods: [] as Food[],
            canAdd: true,
            foodBG1,
        };
    },
    methods: {
        tableRowClassName({ rowIndex }: { rowIndex: number }) {
            return rowIndex % 2 === 1 ? 'warning-row' : 'success-row';
        },
        handleEdit(index: number) {
            this.foods[index].isEditing = true;
        },
        handleDelete(index: number) {
            const row = this.foods[index];
            this.deleteFood(row.foodName);
            this.foods.splice(index, 1);
        },
        onAddItem() {
            if (this.canAdd) {
                this.canAdd = false;
                const newFood: Food = { foodName: '', calorie: 0, isEditing: true };
                this.foods.push(newFood);
                nextTick(() => {
                    const foodTable = this.$refs.foodTable as unknown as TableRef;
                    const table = foodTable.$el.querySelector('.el-table__body-wrapper tbody');
                    if (table) {
                    const rows = table.children;
                    const lastRow = rows[rows.length - 1];
                    lastRow.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }
                });
            }
        },
        saveEdit(index: number) {
            const row = this.foods[index];
            if (this.validateFoodName(row) && this.validateCalorie(row)) {
                row.isEditing = false;
                if (this.canAdd) {
                    this.UpdateFoodToDB(row);
                } else {
                    this.sendFoodToDB(row);
                    this.canAdd = true;
                }
            }
        },
        cancelEdit(index: number) {
            const row = this.foods[index];
            if (row.calorie > 1 && row.calorie < 10000 && row.foodName) {             
                row.isEditing = false;
            } else {
                this.foods.splice(index, 1); // 删除该行
            }
            this.canAdd = true;
        },
        validateCalorie(row: Food) {
            if (row.calorie < 1 || row.calorie > 10000) {
                ElNotification({
                    title: '警告',
                    message: '卡路里必须在1-10000之间',
                    type: 'warning',
                    duration: 2000
                })
                return false;
            }
            return true;
        },
        validateFoodName(row: Food) {
    // 检查食物名称是否为空
    if (!row.foodName.trim()) {
        ElNotification({
            title: '警告',
            message: '食物名称不能为空',
            type: 'warning',
            duration: 2000
        });
        return false;
    }

    // 检查食物名称是否重复
    const duplicate = this.foods.some(
        food => food.foodName.trim() === row.foodName.trim() && food !== row
    );

    if (duplicate) {
        ElNotification({
            title: '警告',
            message: '食物名称不可重复，请重新输入',
            type: 'warning',
            duration: 2000
        });
        row.foodName="";
        return false;
    }

    return true;
},
        getFoodFromDB() {
            mealPlanApi.getFoodsInfo()
                .then(response => {
                    console.log(response.data.data.foodsInfo);
                    response.data.data.foodsInfo.forEach((item: { foodName: string; calories: number }) => {
                        const food: Food = { foodName: item.foodName, calorie: item.calories };
                        this.foods.push(food);
                    });
                    console.log(this.foods);
                });
        },
        sendFoodToDB(food: Food) {
            const requestData = {
                foodName: food.foodName,
                calorie: food.calorie,
            };
            console.log('send', requestData);
            mealPlanApi.insertFoodInfo(requestData)
                .then(response => {
                    console.log(response.data.data.message);
                    // 显示通知
                    ElNotification({
                        message: response.data.data.message,
                        type: 'success',
                        duration: 2000
                    });
                });
        },
        UpdateFoodToDB(food: Food) {
            const requestData = {
                foodName: food.foodName,
                calorie: food.calorie,
            };
            console.log(requestData);
            mealPlanApi.updateFoodInfo(requestData)
                .then(response => {
                    console.log(response.data.data.message);
                    // 显示通知
                    ElNotification({
                        message: response.data.data.message,
                        type: 'success',
                        duration: 2000
                    });
                });
        },
        deleteFood(foodName: string) {
            mealPlanApi.deleteFoodInfo(foodName)
                .then(response => {
                    console.log(response.data.data.message);
                // 显示通知
                ElNotification({
                        message: response.data.data.message,
                    type: 'success',
                    duration: 2000
                });
            });
        },
    },
    created() {
        this.getFoodFromDB();
    },
});
</script>

<style>
.mt-4 {
    margin-top: 1rem;
}

.table {
    border: 1.5px solid #fdf6ec;
    border-radius: 10px;
}

.table-header {
    color: #337ecc;
}

.el-table .el-table__row.warning-row {
    background-color: #fef0f0;
}

.el-table .el-table__row.success-row {
    background-color: #ecf5ff;
}
</style>
