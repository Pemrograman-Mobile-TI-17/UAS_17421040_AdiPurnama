const snack = require('../model/Snack.js')
const response = require('../config/response')
const mongoose = require('mongoose')
const ObjectId = mongoose.Types.ObjectId
exports.inputDataSanck = (data, gambar) =>
    new Promise(async (resolve, reject) =>{

        const snacknew = new snack({
            kodeSnack : data.kodeSnack,
            namaSnack : data.namaSnack,
            hargaSnack: data.hargaSnack,
            gambar: gambar
        })

        await snack.findOne({kodeSnack: data.kodeSnack})
            .then(snack =>{
                if (snack){
                    reject(response.commonErrorMsg('Kode Snack Sudah Digunakan'))
                }else{
                    snacknew.save()
                        .then(r =>{
                            resolve(response.commonSuccessMsg('Berhasil Menginput Data'))
                        }).catch(err =>{
                        reject(response.commonErrorMsg('Mohon Maaf Input Kue Gagal'))
                    })
                }
            }).catch(err =>{
                reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami'))
            })
    })

exports.lihatDataSnack = () =>
    new Promise(async (resolve, reject) =>{
        await snack.find({})
            .then(result =>{
                resolve(response.commonResult(result))
            })
            .catch(()=>reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami')))
    })

exports.lihatDetailDataSnack = (kodeSnack) =>
    new Promise(async (resolve, reject) =>{
        await snack.findOne({kodeSnack: kodeSnack})
            .then(result =>{
                resolve(response.commonResult(result))
            })
            .catch(()=>reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami')))
    })

exports.updateSnack = (id, data, gambar) =>
    new Promise(async (resolve, reject)=>{
        await snack.updateOne(
            {_id : ObjectId(id)},
            {
                $set: {
                    kodeSnack : data.kodeSnack,
                    namaSnack : data.namaSnack,
                    hargaSnack: data.hargaSnack,
                    gambar: gambar
                }
            }
        ).then(snack =>{
            resolve(response.commonSuccessMsg('Berhasil Mengubah Data'))
        }).catch(err =>{
            reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami'))
        })
    })

exports.hapussnack = (_id) =>
    new Promise(async (resolve, reject) =>{
        await snack.remove({_id: ObjectId(_id)})
            .then(() =>{
                resolve(response.commonSuccessMsg('Berhasil Menghapus Data'))
            }).catch(() =>{
                reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami'))
            })
    })