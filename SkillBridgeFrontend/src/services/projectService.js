import api from './api';

export const projectService = {
    getAll: async () => {
        const response = await api.get('/projects');
        return response.data;
    },

    getById: async (id) => {
        const response = await api.get(`/projects/${id}`);
        return response.data;
    },

    create: async (data) => {
        const response = await api.post('/projects/create', data);
        return response.data;
    },

    update: async (id, data) => {
        const response = await api.put(`/projects/update/${id}`, data);
        return response.data;
    },

    patch: async (id, data) => {
        const response = await api.patch(`/projects/patch/${id}`, data);
        return response.data;
    },

    updateStatus: async (id, status) => {
        const response = await api.patch(`/projects/${id}/status`, { status });
        return response.data;
    },

    delete: async (id) => {
        const response = await api.delete(`/projects/delete/${id}`);
        return response.data;
    },

    getApplications: async (id) => {
        const response = await api.get(`/projects/${id}/applications`);
        return response.data;
    },

    modifyMembers: async (id, data) => {
        const response = await api.patch(`/projects/${id}/members`, data);
        return response.data;
    },
};

export default projectService;
