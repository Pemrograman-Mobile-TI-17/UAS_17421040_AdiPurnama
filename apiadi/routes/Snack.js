const multer = require('multer')
const fs = require('fs')
const router = require('express').Router()
const snack = require('../controller/Snack')

var storage = multer.diskStorage({
    filename: function (req, file, cb) {
        let ext = file.originalname.substring(
            file.originalname.lastIndexOf("."),
            file.originalname.length
        )
        cb(null, Date.now() + ext);
    },
    destination: function (req, file, cb){
        cb(null, './gambar')
    }
})

var upload = multer({storage: storage}).single("gambar")

router.post("/input", upload, (req, res) => {

    snack.inputDataSanck(req.body, req.file.filename)
        .then((result)=> res.json(result))
        .catch((err)=> res.json(err))

})

router.get("/datasnack", (req, res) =>{
    snack.lihatDataSnack()
        .then((result)=> res.json(result))
        .catch((err)=> res.json(err))
})

router.get("/datasnack/:id", (req, res) =>{
    snack.lihatDetailDataSnack(req.params.id)
        .then((result)=> res.json(result))
        .catch((err)=> res.json(err))
})

router.delete("/hapus/:id", (req, res) =>{
    snack.hapussnack(req.params.id)
        .then((result)=> res.json(result))
        .catch((err)=> res.json(err))
})

router.put("/ubah/:id", upload, (req, res) =>{
    let fileName;
    if(req.body.gambar){
        fileName = req.body.gambar;
    }else{
        fileName = req.file.filename;
    }
    snack.updateSnack(req.params.id, req.body, fileName)
        .then((result)=> res.json(result))
        .catch((err)=> res.json(err))
})

module.exports = router