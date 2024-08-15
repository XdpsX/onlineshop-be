import Modal from '../Modal'
import { MdOutlineCreateNewFolder } from 'react-icons/md'
import { IoClose } from 'react-icons/io5'
import { FaPlus } from 'react-icons/fa'
import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { BeatLoader } from 'react-spinners'
import {
  addVendor,
  getVendors,
  setShowModal,
  setUpdVendor,
  updateVendor,
} from '../../store/slices/vendorSlice'
import { Field, Form, Formik } from 'formik'
import * as Yup from 'yup'
import { toast } from 'react-toastify'
import { overrideStyle } from '../../utils/cssHelper'

const VendorSchema = Yup.object().shape({
  name: Yup.string()
    .max(128, 'Maximum 128 characters')
    .required('Name is required!'),
})

const AddVendor = () => {
  const dispatch = useDispatch()
  const [logoPreview, setLogoPreview] = useState(null)
  const [error, setError] = useState(null)
  const [isProcessing, setIsProcessing] = useState(false)
  const { showModal, params, updVendor } = useSelector((state) => state.vendor)
  const isUpdating = updVendor != null

  // const initialValues = {
  //   name: '',
  //   logo: null,
  // }

  const initialValues = {
    name: updVendor?.name || '',
    logo: null,
  }

  const closeModal = () => {
    dispatch(setShowModal(false))
    setLogoPreview(null)
    dispatch(setUpdVendor(null))
    setError(null)
  }

  const openModal = () => {
    dispatch(setShowModal(true))
  }

  const validateLogo = (file) => {
    return new Promise((resolve) => {
      if (!isUpdating) {
        if (!file) {
          setError('File is required!')
          resolve(false)
          return
        }
      }
      if (file) {
        const allowedTypes = ['image/jpeg', 'image/png']
        if (!allowedTypes.includes(file.type)) {
          setError('File must be a JPEG or PNG image.')
          resolve(false)
          return
        }

        const img = new Image()
        img.src = URL.createObjectURL(file)
        img.onload = () => {
          if (img.width < 320) {
            setError('Image width must be at least 320px.')
            resolve(false)
          } else {
            setError(null)
            resolve(true)
          }
        }
        img.onerror = () => {
          setError('Failed to load image.')
          resolve(false)
        }
      }
      setError(null)
      resolve(true)
    })
  }

  const submitHandler = (values, { setFieldError, setSubmitting }) => {
    setIsProcessing(true)
    const formData = new FormData()
    formData.append('name', values.name)
    if (values.logo) {
      formData.append('logo', values.logo)
    }

    const processing = isUpdating
      ? updateVendor({ vendorId: updVendor.id, formData })
      : addVendor(formData)

    dispatch(processing)
      .unwrap()
      .then(() => {
        if (isUpdating) {
          toast.success('Update Vendor Successfully')
        } else {
          toast.success('Add Vendor Successfully')
        }

        dispatch(getVendors(params))
      })
      .catch((err) => {
        setError(err.message)
        if (err.details) {
          Object.keys(err.details).forEach((field) => {
            setFieldError(field, err.details[field])
          })
        }
      })
      .finally(() => {
        closeModal()
        setIsProcessing(false)
        setSubmitting(false)
      })
  }

  return (
    <>
      <button
        className="inline-flex items-center bg-green-500 hover:shadow-green-500/4 mb-3 cursor-pointer gap-2 px-2 py-1 rounded-sm"
        type="button"
        onClick={openModal}
      >
        <MdOutlineCreateNewFolder size={28} color="white" />
        <span className="shadow-lg text-white text-md">Add</span>
      </button>
      <Modal isOpen={showModal} onClose={closeModal}>
        <div className="flex items-center justify-between py-2 md:p-5 border-b rounded-t dark:border-gray-600">
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
            {isUpdating ? 'Update Vendor' : 'Create New Vendor'}
          </h3>
          <button
            type="button"
            className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-2xl ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white"
            onClick={closeModal}
          >
            <IoClose />
          </button>
        </div>
        {/* <!-- Modal body --> */}
        <Formik
          initialValues={initialValues}
          validationSchema={VendorSchema}
          onSubmit={async (
            values,
            { setFieldError, resetForm, setSubmitting }
          ) => {
            const isValid = await validateLogo(values.logo)
            if (isValid) {
              submitHandler(values, { setFieldError, resetForm, setSubmitting })
            } else {
              setSubmitting(false)
            }
          }}
          className="p-4 md:p-5"
        >
          {({ errors, touched, setFieldValue }) => (
            <Form>
              {error && (
                <p className="text-md font-bold text-red-600 text-center mb-2">
                  {error}
                </p>
              )}
              <div className="gap-4 mb-6">
                <label
                  htmlFor="name"
                  className="block mb-2 text-md font-medium text-gray-900 dark:text-white"
                >
                  Name
                </label>
                <Field
                  type="text"
                  name="name"
                  id="name"
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                  placeholder="Type category name"
                  required
                />
                {errors.name && touched.name && (
                  <p className="text-sm text-red-600">{errors.name}</p>
                )}
              </div>

              <div className="gap-4 mb-6">
                <label
                  htmlFor="logo"
                  className="block mb-2 text-md font-medium text-gray-900 dark:text-white"
                >
                  Logo
                </label>
                <input
                  type="file"
                  name="logo"
                  id="logo"
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                  onChange={(event) => {
                    const file = event.currentTarget.files[0]
                    setFieldValue('logo', file)
                    setLogoPreview(URL.createObjectURL(file)) // Tạo URL để preview logo
                  }}
                />
                {errors.logo && touched.logo && (
                  <p className="text-sm text-red-600">{errors.logo}</p>
                )}
              </div>

              {/* Hiển thị logo preview */}
              {logoPreview && (
                <div className="mb-4">
                  <img
                    src={logoPreview}
                    alt="Logo Preview"
                    className="h-28 w-28 object-cover rounded"
                  />
                </div>
              )}
              {isProcessing ? (
                <button
                  type="submit"
                  className="text-white gap-2 inline-flex items-center bg-gray-700 focus:ring-4 focus:outline-non font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                  disabled={true}
                >
                  <BeatLoader
                    color="#fff"
                    size={12}
                    cssOverride={overrideStyle}
                  />
                </button>
              ) : (
                <button
                  type="submit"
                  className="text-white gap-2 inline-flex items-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                >
                  <FaPlus />
                  {isUpdating ? 'Update' : 'Add'}
                </button>
              )}
            </Form>
          )}
        </Formik>
      </Modal>
    </>
  )
}
export default AddVendor
