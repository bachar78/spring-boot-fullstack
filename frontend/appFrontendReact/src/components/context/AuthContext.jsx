import {useEffect, useState, useContext, createContext} from "react";
import {login as performLogin} from "../../services/client.js";

import {jwtDecode} from "jwt-decode"


const AuthContext = createContext({})
const AuthProvider = ({children}) => {
    const [customer, setCustomer] = useState(null)


    const setCustomerFromToken = (token) => {
        const decodedToken = jwtDecode(token);
        setCustomer({
            email: decodedToken.sub,
            roles: decodedToken.scopes
        })
    }

    useEffect(() => {
        const token = localStorage.getItem("access_token")
        if(token) {
            setCustomerFromToken(token);
        }
    }, []);
    const login = async (emailAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(emailAndPassword).then(res => {
                const jwt = res.headers["authorization"];
                localStorage.setItem("access_token", jwt);
                const decodedJwt = jwtDecode(jwt);
                setCustomer({
                    email: decodedJwt.sub,
                    roles: decodedJwt.scopes
                })
                resolve(res)
            }).catch(err => {
                reject(err);
            })
        })
    }

    const logout = () => {
        localStorage.removeItem("access_token")
        setCustomer(null)
    }

    const isCustomerAuthenticated = () => {
        const token = localStorage.getItem("access_token")
        if (!token) {
            return false;
        }
        const {exp: expirationDate} = jwtDecode(token);
        if (Date.now() > expirationDate * 1000) {
            logout()
            return false;
        }
        return true;
    }


    return (
        <AuthContext.Provider value={{
            customer, login, logout, isCustomerAuthenticated, setCustomerFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )
}


export const useAuth = () => useContext(AuthContext)
export default AuthProvider;