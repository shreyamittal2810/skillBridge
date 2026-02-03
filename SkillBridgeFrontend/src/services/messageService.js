import api from './api';

export const messageService = {
    // Send a direct message
    send: async (receiverStudentId, message) => {
        const response = await api.post('/direct-messages', {
            receiverStudentId,
            message,
        });
        return response.data;
    },

    // Get all my messages
    getMyMessages: async () => {
        const response = await api.get('/direct-messages/me');
        return response.data;
    },

    // Update a message
    update: async (id, message) => {
        const response = await api.put(`/direct-messages/${id}`, { message });
        return response.data;
    },

    // Delete a message
    delete: async (id) => {
        const response = await api.delete(`/direct-messages/${id}`);
        return response.data;
    },

    // Get all students for dropdown
    getAllStudents: async () => {
        const response = await api.get('/students/all');
        return response.data;
    },
};

export default messageService;
