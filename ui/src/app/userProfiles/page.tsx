'use client';

import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import Modal from "@/components/Modal";
import UserProfilesApiClient, {UserProfile} from "@/utils/UserProfilesApiClient";

const UserProfiles = () => {
    const apiBaseUri = process.env.NEXT_PUBLIC_API_BASE_URI;

    const {user} = useAuth();
    const userProfilesApiClient = new UserProfilesApiClient(user?.access_token, apiBaseUri);

    const [userProfiles, setUserProfiles] = useState<PagedData<UserProfile> | null>();
    const [editModalShown, setEditModalShown] = useState(false);
    const [editModalData, setEditModalData] = useState<any | null | undefined>(null);
    const [addModalShown, setAddModalShown] = useState(false);
    const [addModalData, setAddModalData] = useState<any | null | undefined>(null);

    useEffect(() => {
        const init = async () => {
            try {
                setUserProfiles(await userProfilesApiClient.get());
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void init();
        } catch (e) {
            console.error(e);
        }
    }, [user?.profile?.preferred_username]);

    return (
        <Content title={`User Profiles`}>
            <Table<UserProfile>
                data={userProfiles}
                searchLabel={`Username`}
                onSearch={async (v) => {
                    try {
                        setUserProfiles(await userProfilesApiClient.getByUsername(v));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onAdd={() => {
                    setAddModalData({
                        id: ``,
                        username: ``,
                        familyName: ``,
                        givenName: ``,
                        age: ``,
                        gender: ``,
                        profile: ``,
                    });

                    setAddModalShown(true);
                }}
                onEdit={(data) => {
                    setEditModalData(data);

                    setEditModalShown(true);
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setUserProfiles(await userProfilesApiClient.getByUsername(keyword, page));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
            <Modal open={editModalShown}
                   data={editModalData}
                   readonly={[`id`, `username`]}
                   title={`Edit User Profile`}
                   onSave={async (data) => {
                       const existedUserProfile = (userProfiles?.content ?? []).find(d => data.id === d.id);

                       if (!existedUserProfile) {
                           return;
                       }

                       try {
                           const patchUserProfile: UserProfile = {
                               id: existedUserProfile.id,
                               familyName: data.familyName ?? existedUserProfile.familyName,
                               givenName: data.givenName ?? existedUserProfile.givenName,
                               age: data.age ?? existedUserProfile.age,
                               gender: data.gender ?? existedUserProfile.gender,
                               profile: data.profile ?? existedUserProfile.profile,
                           } as UserProfile

                           await userProfilesApiClient.patchByUsername(
                               existedUserProfile.username, patchUserProfile);

                           setUserProfiles(await userProfilesApiClient.get());
                           setEditModalShown(false);
                       } catch (e) {
                           console.error(e);
                       }
                   }}
                   onCancel={() => {
                       setEditModalShown(false);
                   }}
            />
            <Modal open={addModalShown}
                   data={addModalData}
                   title={`Add User Profile`}
                   onSave={async (data) => {
                       if (!data.id) {
                           return;
                       }

                       try {
                           const newData = await userProfilesApiClient.post({
                               id: data.id,
                               username: data.username,
                               familyName: data.familyName,
                               givenName: data.givenName,
                               age: data.age,
                               gender: data.gender,
                               profile: data.profile,
                           });
                           setUserProfiles({
                               ...userProfiles,
                               content: [
                                   ...(userProfiles?.content ?? []),
                                   newData,
                               ]
                           } as PagedData<UserProfile>);

                           setAddModalShown(false);
                       } catch (e) {
                           console.error(e);
                       }
                   }}
                   onCancel={() => {
                       setAddModalShown(false);
                   }}
            />
        </Content>
    );
}

export default UserProfiles;
