import api from './api';

export const adminService = {
    // Get all students
    getAllStudents: async () => {
        const response = await api.get('/admin/students');
        return response.data;
    },

    // Get student by ID
    getStudentById: async (id) => {
        const response = await api.get(`/admin/students/${id}`);
        return response.data;
    },

    // Update student role
    updateRole: async (id, role) => {
        const response = await api.patch(`/admin/students/${id}/role?role=${role}`);
        return response.data;
    },

    // Delete student
    deleteStudent: async (id) => {
        const response = await api.delete(`/admin/students/${id}`);
        return response.data;
    },

    // Get all projects
    getAllProjects: async () => {
        const response = await api.get('/admin/projects');
        return response.data;
    },

    // Update project status
    updateProjectStatus: async (id, status) => {
        const response = await api.patch(`/admin/projects/${id}/status?status=${status}`);
        return response.data;
    },

    // Delete project
    deleteProject: async (id) => {
        const response = await api.delete(`/admin/projects/${id}`);
        return response.data;
    },
};

export default adminService;
