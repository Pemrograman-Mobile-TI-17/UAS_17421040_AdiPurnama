const mongoose = require('mongoose');

const userSchema = mongoose.Schema({

    kodeSnack: {
        type: String
    },
    namaSnack: {
        type: String
    },
    hargaSnack: {
        type: String
    },
    gambar: {
        type: String
    }
})
module.exports = mongoose.model('snack', userSchema)