import {useEffect, useState, useContext, createContext} from "react";
import {login as performLogin} from "../../services/client.js";

import {jwtDecode} from "jwt-decode"


const AuthContext = createContext({})
const AuthProvider = ({children}) => {
    const [customer, setCustomer] = useState(null)


    useEffect(() => {
        let token = localStorage.getItem("access_token")
        if(token) {
            token = jwtDecode(token);
            setCustomer({
                email: token.sub,
                roles: token.scopes
            })
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
            customer, login, logout, isCustomerAuthenticated
        }}>
            {children}
        </AuthContext.Provider>
    )
}


export const useAuth = () => useContext(AuthContext)
export default AuthProvider;