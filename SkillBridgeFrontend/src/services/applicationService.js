import api from './api';

export const applicationService = {
    apply: async (data) => {
        const response = await api.post('/applications/create', data);
        return response.data;
    },

    getById: async (id) => {
        const response = await api.get(`/applications/${id}`);
        return response.data;
    },

    getMyApplications: async () => {
        const response = await api.get('/applications');
        return response.data;
    },

    getForProject: async (projectId) => {
        const response = await api.get(`/applications/project/${projectId}`);
        return response.data;
    },

    updateMessage: async (id, data) => {
        const response = await api.patch(`/applications/update/${id}`, data);
        return response.data;
    },

    updateStatus: async (id, status) => {
        const response = await api.patch(`/applications/update/${id}/status`, { status });
        return response.data;
    },

    delete: async (id) => {
        const response = await api.delete(`/applications/delete/${id}`);
        return response.data;
    },
};

export default applicationService;
