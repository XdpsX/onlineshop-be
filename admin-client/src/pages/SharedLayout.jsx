import { useEffect, useState } from 'react'
import { Outlet } from 'react-router-dom'
import { Header, Loading, Sidebar } from '../components'
import { useDispatch, useSelector } from 'react-redux'
import { getUserProfile } from '../store/slices/authSlice'

const SharedLayout = () => {
  const dispatch = useDispatch()
  const { userProfile } = useSelector((state) => state.auth)
  const [showSidebar, setShowSidebar] = useState(false)

  useEffect(() => {
    if (!userProfile) {
      dispatch(getUserProfile())
    }
  }, [userProfile, dispatch])

  if (!userProfile) {
    return <Loading />
  }
  return (
    <div className="bg-[#cdcae9] w-full min-h-screen">
      <Header showSidebar={showSidebar} setShowSidebar={setShowSidebar} />
      <Sidebar showSidebar={showSidebar} setShowSidebar={setShowSidebar} />

      <div className="ml-0 lg:ml-[260px] pt-[95px] transition-all">
        <Outlet />
      </div>
    </div>
  )
}

export default SharedLayout
