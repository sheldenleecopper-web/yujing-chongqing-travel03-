<template>
  <div class="manage">
    <div class="toolbar">
      <el-button type="primary" @click="openAdd">新增酒店</el-button>
    </div>
    <el-table :data="list" border stripe style="margin-top:16px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="酒店名称" min-width="160" />
      <el-table-column prop="region" label="区域" width="120" />
      <el-table-column prop="priceRange" label="价格区间" width="110" />
      <el-table-column prop="intro" label="推荐理由" min-width="200" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑酒店' : '新增酒店'" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="酒店名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" />
        </el-form-item>
        <el-form-item label="价格区间" prop="priceRange">
          <el-select v-model="form.priceRange" style="width:100%">
            <el-option label="经济" value="经济" />
            <el-option label="适中" value="适中" />
            <el-option label="豪华" value="豪华" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐理由">
          <el-input v-model="form.intro" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminHotelList, addAdminHotel, updateAdminHotel, deleteAdminHotel } from '../../api'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = ref({})
const rules = {
  name: [{ required: true, message: '请输入酒店名称', trigger: 'blur' }],
  region: [{ required: true, message: '请输入区域', trigger: 'blur' }],
  priceRange: [{ required: true, message: '请选择价格区间', trigger: 'change' }]
}

const loadList = async () => {
  loading.value = true
  list.value = await getAdminHotelList()
  loading.value = false
}

const openAdd = () => {
  isEdit.value = false
  form.value = { priceRange: '适中' }
  dialogVisible.value = true
}

const openEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate(async valid => {
    if (!valid) return
    try {
      if (isEdit.value) {
        await updateAdminHotel(form.value.id, form.value)
      } else {
        await addAdminHotel(form.value)
      }
      ElMessage.success('操作成功')
      dialogVisible.value = false
      loadList()
    } catch (e) {}
  })
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteAdminHotel(row.id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.manage { padding: 0; }
.toolbar { display: flex; justify-content: flex-end; }
</style>
