<template>
  <div class="manage">
    <div class="toolbar">
      <el-button type="primary" @click="openAdd">新增非遗</el-button>
    </div>
    <el-table :data="list" border stripe style="margin-top:16px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="项目名称" min-width="140" />
      <el-table-column prop="level" label="级别" width="90" />
      <el-table-column prop="category" label="类别" width="120" />
      <el-table-column prop="inheritor" label="传承人" width="100" />
      <el-table-column prop="experienceSpot" label="体验地点" min-width="140" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑非遗' : '新增非遗'" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="级别" prop="level">
          <el-select v-model="form.level" style="width:100%">
            <el-option label="国家级" value="国家级" />
            <el-option label="市级" value="市级" />
          </el-select>
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="封面图">
          <el-input v-model="form.cover" />
        </el-form-item>
        <el-form-item label="传承人">
          <el-input v-model="form.inheritor" />
        </el-form-item>
        <el-form-item label="体验地点">
          <el-input v-model="form.experienceSpot" />
        </el-form-item>
        <el-form-item label="关联去处ID">
          <el-input-number v-model="form.relatedSpotId" :min="0" />
        </el-form-item>
        <el-form-item label="简介">
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
import { getAdminHeritageList, addAdminHeritage, updateAdminHeritage, deleteAdminHeritage } from '../../api'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = ref({})
const rules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  level: [{ required: true, message: '请选择级别', trigger: 'change' }]
}

const loadList = async () => {
  loading.value = true
  list.value = await getAdminHeritageList()
  loading.value = false
}

const openAdd = () => {
  isEdit.value = false
  form.value = { level: '国家级' }
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
        await updateAdminHeritage(form.value.id, form.value)
      } else {
        await addAdminHeritage(form.value)
      }
      ElMessage.success('操作成功')
      dialogVisible.value = false
      loadList()
    } catch (e) {}
  })
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteAdminHeritage(row.id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.manage { padding: 0; }
.toolbar { display: flex; justify-content: flex-end; }
</style>
