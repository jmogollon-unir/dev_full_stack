import {Route, Routes} from 'react-router-dom'
import {PrivateRoutes} from './PrivateRoutes'
import MainLayout from '../Layout/MainLayout'
import Home from '../pages/Home/Home.jsx'
import BookDetail from '../pages/BookDetail/BookDetail.jsx'
import Checkout from '../pages/Checkout/Checkout.jsx'
import Login from '../pages/Login/Login.jsx'
import Profile from '../pages/Profile/Profile.jsx'
import Landing from "../pages/Landing/Landing.jsx";

function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Landing/>}/>
            <Route path="/login" element={<Login/>}/>
            <Route element={<MainLayout/>}>
                <Route path="/books" element={<Home/>}/>
                <Route path="/books/:bookId" element={<BookDetail/>}/>
                <Route
                    path="/checkout"
                    element={
                        <PrivateRoutes>
                            <Checkout/>
                        </PrivateRoutes>
                    }
                />
                <Route
                    path="/profile"
                    element={
                        <PrivateRoutes>
                            <Profile/>
                        </PrivateRoutes>
                    }
                />
            </Route>
        </Routes>
    )
}

export default AppRoutes
